function initializeCoreMod() {
	Opcodes = Java.type("org.objectweb.asm.Opcodes");

	VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
	MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
	InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
	InsList = Java.type("org.objectweb.asm.tree.InsnList");
	LabelNode = Java.type("org.objectweb.asm.tree.LabelNode");

	INVOKESTATIC = Opcodes.INVOKESTATIC;
	ALOAD = Opcodes.ALOAD;
	FRETURN = Opcodes.FRETURN;
	
	return {
		"ChunkStatus#doGenerationWork" : {
			"target" : {
				"type" : "METHOD",
				"class" : "net.minecraft.world.chunk.ChunkStatus",
				"methodName" : "func_223198_a",
				"methodDesc" : "(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/world/server/ServerWorldLightManager;Ljava/util/function/Function;Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"
			},
			"transformer" : function(methodNode) {
				injectHook(methodNode.instructions);
				return methodNode;
			}
		}
	}
}

function injectHook(instructions) {
	list("BEFORE ", instructions)

	var beforeList = new InsList();
	
	beforeList.add(new LabelNode())

	beforeList.add(new VarInsnNode(ALOAD, 0)); // this
	beforeList.add(new MethodInsnNode(INVOKESTATIC, "info/u_team/world_generation_profiler/hook/TestHook", "hook", "(Lnet/minecraft/world/chunk/ChunkStatus;)V", false));
	
	beforeList.add(new LabelNode())
	
	instructions.insert(beforeList)
	
	list("AFTER ", instructions)
}

function list(be, instructions) {
	print (be + "______________________________________________________________________________________________________")
	
	instructions.forEach(function (item, index) {
		print(item, index);
	});
}
