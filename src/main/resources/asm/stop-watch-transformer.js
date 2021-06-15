function initializeCoreMod() {
	Opcodes = Java.type("org.objectweb.asm.Opcodes")
	
	ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI")
	
	AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode")
	VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode")
	MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode")
	InsnNode = Java.type("org.objectweb.asm.tree.InsnNode")
	InsList = Java.type("org.objectweb.asm.tree.InsnList")
	LabelNode = Java.type("org.objectweb.asm.tree.LabelNode")
	
	LINE = AbstractInsnNode.LINE
	
	INVOKESTATIC = Opcodes.INVOKESTATIC
	INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL
	ALOAD = Opcodes.ALOAD
	ASTORE = Opcodes.ASTORE
	POP = Opcodes.POP
	DUP = Opcodes.DUP
	NOOPCODE = -1

	return {
		"ChunkStatus#doGenerationWork" : {
			"target" : {
				"type" : "METHOD",
				"class" : "net.minecraft.world.chunk.ChunkStatus",
				"methodName" : "func_223198_a",
				"methodDesc" : "(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/world/server/ServerWorldLightManager;Ljava/util/function/Function;Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"
			},
			"transformer" : function(methodNode) {
				injectTimerAtDoGenerationWork(methodNode, methodNode.instructions);
				return methodNode;
			}
		}
	}
}

function injectTimerAtDoGenerationWork(methodNode, instructions) {
	var doWorkMethodNode = ASMAPI.findFirstMethodCall(
			methodNode, 
			ASMAPI.MethodType.INTERFACE, 
			"net/minecraft/world/chunk/ChunkStatus$IGenerationWorker", 
			ASMAPI.mapMethod("doWork"),  // ChunkStatus$IGenerationWorker.doWork
			"(Lnet/minecraft/world/chunk/ChunkStatus;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/world/server/ServerWorldLightManager;Ljava/util/function/Function;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)Ljava/util/concurrent/CompletableFuture;"
	)
	if(doWorkMethodNode == null) {
		throw "Could not find call to ChunkStatus$IGenerationWorker.doWork"
	}	
	
	insertStartTimerNode = findInstructionTypeBefore(instructions, LINE, NOOPCODE, doWorkMethodNode)
	if(findInstructionTypeBefore == null) {
		throw "The insert start timer node could not be found"
	}
	
	var startTimerInsList = new InsList()
	startTimerInsList.add(createStartTimerCode(8)) // Inject Stopwatch.createNew and store on local variable 16
	instructions.insert(insertStartTimerNode, startTimerInsList)
	
	var storeChunkValueInsList = new InsList()
	storeChunkValueInsList.add(new InsnNode(DUP)) // Duplicate current stack stop
	storeChunkValueInsList.add(new VarInsnNode(ASTORE, 9)) // Store current stack top (IChunk) before the doWork method is called on local variable 14
	instructions.insertBefore(doWorkMethodNode, storeChunkValueInsList)
	
	var stopTimerInsList = new InsList()
	stopTimerInsList.add(new VarInsnNode(ASTORE, 10)) // Store return value of doWork method in local variable 15
	
	stopTimerInsList.add(createStopTimerCode(8)) // Inject Stopwatch.stop code
	
	stopTimerInsList.add(new VarInsnNode(ALOAD, 0)) // Load this
	stopTimerInsList.add(new VarInsnNode(ALOAD, 9)) // Load IChunk value
	stopTimerInsList.add(new VarInsnNode(ALOAD, 8)) // Load stopwatch
	stopTimerInsList.add(ASMAPI.buildMethodCall(
			"info/u_team/world_generation_profiler/hook/StopWatchHook",
			"doGenerationWorkHook",
			"(Lnet/minecraft/world/chunk/ChunkStatus;Lnet/minecraft/world/chunk/IChunk;Lcom/google/common/base/Stopwatch;)V",
			ASMAPI.MethodType.STATIC
	)) // Inject method call to our hook
	
	stopTimerInsList.add(new VarInsnNode(ALOAD, 10)) // Load the original return value from local variable 15 that will be returned
	
	instructions.insert(doWorkMethodNode, stopTimerInsList)
	 
	//printInstructions(instructions)	// Debug
}

function createStartTimerCode(local) {
	var insList = new InsList()
	
	insList.add(ASMAPI.buildMethodCall(
			"com/google/common/base/Stopwatch",
			"createStarted",
			"()Lcom/google/common/base/Stopwatch;",
			ASMAPI.MethodType.STATIC
	))
	insList.add(new VarInsnNode(ASTORE, local))
	return insList;
}

function createStopTimerCode(local) {
	var insList = new InsList()
	
	insList.add(new VarInsnNode(ALOAD, local))
	insList.add(ASMAPI.buildMethodCall(
			"com/google/common/base/Stopwatch",
			"stop",
			"()Lcom/google/common/base/Stopwatch;",
			ASMAPI.MethodType.VIRTUAL
	))
	insList.add(new InsnNode(POP))
	return insList;
}

function findInstructionTypeBefore(instructions, type, opcode, startNode) {
	for (var index = Math.min(instructions.size() - 1, instructions.indexOf(startNode)); index >= 0; index--) {
		var node = instructions.get(index);
		if (node.getType() == type && node.getOpcode() == opcode) {
			return node;
		}
	}
	return null;
}

// ------------------------------------------------
// DEBUG
// ------------------------------------------------

function printInstructions(instructions) {
	instructions.forEach(function (node) {
		printNode(node)
	})
}

function printNode(node) {
	var name = getNameFromNode(node)
	if(name.equals("ERROR")) {
		print (node)
	} else {
		if(node.getType() == 5) {
			print (name + " : " + node.owner + "." +node.name + node.desc)
		} else if(node.getType() == 2) {
			print (name + " " + node.var)
		} else {
			print (name)
		}
	}
}

function getNameFromNode(node) {
	var value = "ERROR";
	opcodes.forEach(function (opcode) {
		if(opcode.code == node.getOpcode()) {
			value = opcode.name;
			return;
		}
	})
	return value
}

var opcodes = [{
	    name: "NOP",
	    code: 0
	},
	{
	    name: "ACONST_NULL",
	    code: 1
	},
	{
	    name: "ICONST_M1",
	    code: 2
	},
	{
	    name: "ICONST_0",
	    code: 3
	},
	{
	    name: "ICONST_1",
	    code: 4
	},
	{
	    name: "ICONST_2",
	    code: 5
	},
	{
	    name: "ICONST_3",
	    code: 6
	},
	{
	    name: "ICONST_4",
	    code: 7
	},
	{
	    name: "ICONST_5",
	    code: 8
	},
	{
	    name: "LCONST_0",
	    code: 9
	},
	{
	    name: "LCONST_1",
	    code: 10
	},
	{
	    name: "FCONST_0",
	    code: 11
	},
	{
	    name: "FCONST_1",
	    code: 12
	},
	{
	    name: "FCONST_2",
	    code: 13
	},
	{
	    name: "DCONST_0",
	    code: 14
	},
	{
	    name: "DCONST_1",
	    code: 15
	},
	{
	    name: "BIPUSH",
	    code: 16
	},
	{
	    name: "SIPUSH",
	    code: 17
	},
	{
	    name: "LDC",
	    code: 18
	},
	{
	    name: "ILOAD",
	    code: 21
	},
	{
	    name: "LLOAD",
	    code: 22
	},
	{
	    name: "FLOAD",
	    code: 23
	},
	{
	    name: "DLOAD",
	    code: 24
	},
	{
	    name: "ALOAD",
	    code: 25
	},
	{
	    name: "IALOAD",
	    code: 46
	},
	{
	    name: "LALOAD",
	    code: 47
	},
	{
	    name: "FALOAD",
	    code: 48
	},
	{
	    name: "DALOAD",
	    code: 49
	},
	{
	    name: "AALOAD",
	    code: 50
	},
	{
	    name: "BALOAD",
	    code: 51
	},
	{
	    name: "CALOAD",
	    code: 52
	},
	{
	    name: "SALOAD",
	    code: 53
	},
	{
	    name: "ISTORE",
	    code: 54
	},
	{
	    name: "LSTORE",
	    code: 55
	},
	{
	    name: "FSTORE",
	    code: 56
	},
	{
	    name: "DSTORE",
	    code: 57
	},
	{
	    name: "ASTORE",
	    code: 58
	},
	{
	    name: "IASTORE",
	    code: 79
	},
	{
	    name: "LASTORE",
	    code: 80
	},
	{
	    name: "FASTORE",
	    code: 81
	},
	{
	    name: "DASTORE",
	    code: 82
	},
	{
	    name: "AASTORE",
	    code: 83
	},
	{
	    name: "BASTORE",
	    code: 84
	},
	{
	    name: "CASTORE",
	    code: 85
	},
	{
	    name: "SASTORE",
	    code: 86
	},
	{
	    name: "POP",
	    code: 87
	},
	{
	    name: "POP2",
	    code: 88
	},
	{
	    name: "DUP",
	    code: 89
	},
	{
	    name: "DUP_X1",
	    code: 90
	},
	{
	    name: "DUP_X2",
	    code: 91
	},
	{
	    name: "DUP2",
	    code: 92
	},
	{
	    name: "DUP2_X1",
	    code: 93
	},
	{
	    name: "DUP2_X2",
	    code: 94
	},
	{
	    name: "SWAP",
	    code: 95
	},
	{
	    name: "IADD",
	    code: 96
	},
	{
	    name: "LADD",
	    code: 97
	},
	{
	    name: "FADD",
	    code: 98
	},
	{
	    name: "DADD",
	    code: 99
	},
	{
	    name: "ISUB",
	    code: 100
	},
	{
	    name: "LSUB",
	    code: 101
	},
	{
	    name: "FSUB",
	    code: 102
	},
	{
	    name: "DSUB",
	    code: 103
	},
	{
	    name: "IMUL",
	    code: 104
	},
	{
	    name: "LMUL",
	    code: 105
	},
	{
	    name: "FMUL",
	    code: 106
	},
	{
	    name: "DMUL",
	    code: 107
	},
	{
	    name: "IDIV",
	    code: 108
	},
	{
	    name: "LDIV",
	    code: 109
	},
	{
	    name: "FDIV",
	    code: 110
	},
	{
	    name: "DDIV",
	    code: 111
	},
	{
	    name: "IREM",
	    code: 112
	},
	{
	    name: "LREM",
	    code: 113
	},
	{
	    name: "FREM",
	    code: 114
	},
	{
	    name: "DREM",
	    code: 115
	},
	{
	    name: "INEG",
	    code: 116
	},
	{
	    name: "LNEG",
	    code: 117
	},
	{
	    name: "FNEG",
	    code: 118
	},
	{
	    name: "DNEG",
	    code: 119
	},
	{
	    name: "ISHL",
	    code: 120
	},
	{
	    name: "LSHL",
	    code: 121
	},
	{
	    name: "ISHR",
	    code: 122
	},
	{
	    name: "LSHR",
	    code: 123
	},
	{
	    name: "IUSHR",
	    code: 124
	},
	{
	    name: "LUSHR",
	    code: 125
	},
	{
	    name: "IAND",
	    code: 126
	},
	{
	    name: "LAND",
	    code: 127
	},
	{
	    name: "IOR",
	    code: 128
	},
	{
	    name: "LOR",
	    code: 129
	},
	{
	    name: "IXOR",
	    code: 130
	},
	{
	    name: "LXOR",
	    code: 131
	},
	{
	    name: "IINC",
	    code: 132
	},
	{
	    name: "I2L",
	    code: 133
	},
	{
	    name: "I2F",
	    code: 134
	},
	{
	    name: "I2D",
	    code: 135
	},
	{
	    name: "L2I",
	    code: 136
	},
	{
	    name: "L2F",
	    code: 137
	},
	{
	    name: "L2D",
	    code: 138
	},
	{
	    name: "F2I",
	    code: 139
	},
	{
	    name: "F2L",
	    code: 140
	},
	{
	    name: "F2D",
	    code: 141
	},
	{
	    name: "D2I",
	    code: 142
	},
	{
	    name: "D2L",
	    code: 143
	},
	{
	    name: "D2F",
	    code: 144
	},
	{
	    name: "I2B",
	    code: 145
	},
	{
	    name: "I2C",
	    code: 146
	},
	{
	    name: "I2S",
	    code: 147
	},
	{
	    name: "LCMP",
	    code: 148
	},
	{
	    name: "FCMPL",
	    code: 149
	},
	{
	    name: "FCMPG",
	    code: 150
	},
	{
	    name: "DCMPL",
	    code: 151
	},
	{
	    name: "DCMPG",
	    code: 152
	},
	{
	    name: "IFEQ",
	    code: 153
	},
	{
	    name: "IFNE",
	    code: 154
	},
	{
	    name: "IFLT",
	    code: 155
	},
	{
	    name: "IFGE",
	    code: 156
	},
	{
	    name: "IFGT",
	    code: 157
	},
	{
	    name: "IFLE",
	    code: 158
	},
	{
	    name: "IF_ICMPEQ",
	    code: 159
	},
	{
	    name: "IF_ICMPNE",
	    code: 160
	},
	{
	    name: "IF_ICMPLT",
	    code: 161
	},
	{
	    name: "IF_ICMPGE",
	    code: 162
	},
	{
	    name: "IF_ICMPGT",
	    code: 163
	},
	{
	    name: "IF_ICMPLE",
	    code: 164
	},
	{
	    name: "IF_ACMPEQ",
	    code: 165
	},
	{
	    name: "IF_ACMPNE",
	    code: 166
	},
	{
	    name: "GOTO",
	    code: 167
	},
	{
	    name: "JSR",
	    code: 168
	},
	{
	    name: "RET",
	    code: 169
	},
	{
	    name: "TABLESWITCH",
	    code: 170
	},
	{
	    name: "LOOKUPSWITCH",
	    code: 171
	},
	{
	    name: "IRETURN",
	    code: 172
	},
	{
	    name: "LRETURN",
	    code: 173
	},
	{
	    name: "FRETURN",
	    code: 174
	},
	{
	    name: "DRETURN",
	    code: 175
	},
	{
	    name: "ARETURN",
	    code: 176
	},
	{
	    name: "RETURN",
	    code: 177
	},
	{
	    name: "GETSTATIC",
	    code: 178
	},
	{
	    name: "PUTSTATIC",
	    code: 179
	},
	{
	    name: "GETFIELD",
	    code: 180
	},
	{
	    name: "PUTFIELD",
	    code: 181
	},
	{
	    name: "INVOKEVIRTUAL",
	    code: 182
	},
	{
	    name: "INVOKESPECIAL",
	    code: 183
	},
	{
	    name: "INVOKESTATIC",
	    code: 184
	},
	{
	    name: "INVOKEINTERFACE",
	    code: 185
	},
	{
	    name: "INVOKEDYNAMIC",
	    code: 186
	},
	{
	    name: "NEW",
	    code: 187
	},
	{
	    name: "NEWARRAY",
	    code: 188
	},
	{
	    name: "ANEWARRAY",
	    code: 189
	},
	{
	    name: "ARRAYLENGTH",
	    code: 190
	},
	{
	    name: "ATHROW",
	    code: 191
	},
	{
	    name: "CHECKCAST",
	    code: 192
	},
	{
	    name: "INSTANCEOF",
	    code: 193
	},
	{
	    name: "MONITORENTER",
	    code: 194
	},
	{
	    name: "MONITOREXIT",
	    code: 195
	},
	{
	    name: "MULTIANEWARRAY",
	    code: 197
	},
	{
	    name: "IFNULL",
	    code: 198
	},
	{
	    name: "IFNONNULL",
	    code: 199
	}
]