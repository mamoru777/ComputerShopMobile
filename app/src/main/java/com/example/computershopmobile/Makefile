$(info $(SHELL))
USER_PROTO_OUT_DIR = pkg
USER_API_PATH = gRPC

.PHONY: gen-user

gen-user:

	protoc \
		-I ${USER_API_PATH} \
		--java_out=$(USER_PROTO_OUT_DIR)  \
		./${USER_API_PATH}/*.proto