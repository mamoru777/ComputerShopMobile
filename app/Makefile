$(info $(SHELL))
USER_PROTO_OUT_DIR = app/src/main/java/com/example/computershopmobile/pkg
USER_API_PATH = app/src/main/proto

.PHONY: gen-user

gen-user:

	protoc \
		-I ${USER_API_PATH} \
		--java_out=$(USER_PROTO_OUT_DIR)  \
		--plugin=protoc-gen-grpc-java \
		--grpc-java_out=$(USER_PROTO_OUT_DIR) \
		./${USER_API_PATH}/*.proto