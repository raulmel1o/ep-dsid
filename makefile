compile:
	javac -d bin -cp lib/lombok-1.18.32.jar  src/main/java/br/usp/raulmello/ui/Logger.java src/main/java/br/usp/raulmello/ui/ParametersFileReader.java src/main/java/br/usp/raulmello/utils/Address.java src/main/java/br/usp/raulmello/ui/MenuWriter.java src/main/java/br/usp/raulmello/utils/Operation.java src/main/java/br/usp/raulmello/utils/Message.java src/main/java/br/usp/raulmello/outbound/Outbox.java src/main/java/br/usp/raulmello/DfsContext.java src/main/java/br/usp/raulmello/utils/NodeStats.java src/main/java/br/usp/raulmello/inbound/Dispatcher.java src/main/java/br/usp/raulmello/Node.java src/main/java/br/usp/raulmello/ui/MenuHandler.java src/main/java/br/usp/raulmello/utils/MessageFactory.java src/main/java/br/usp/raulmello/inbound/handlers/AbstractHandler.java src/main/java/br/usp/raulmello/inbound/handlers/ByeHandler.java src/main/java/br/usp/raulmello/inbound/handlers/DepthFirstSearchHandler.java src/main/java/br/usp/raulmello/inbound/handlers/FloodingSearchHandler.java src/main/java/br/usp/raulmello/inbound/handlers/HelloHandler.java src/main/java/br/usp/raulmello/inbound/handlers/RandomWalkSearchHandler.java src/main/java/br/usp/raulmello/inbound/handlers/ValHandler.java src/main/java/br/usp/raulmello/Main.java

package:
	cd bin;jar cfvm ../out/app.jar ../MANIFEST.MF br/usp/raulmello/

clean:
	rm -rf bin/*

.PHONY: all clean cleanall
