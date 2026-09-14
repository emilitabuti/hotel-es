.DEFAULT_GOAL := compile

#- Compila o codigo Java para build/
compile:
	@mkdir -p build
	javac -d build hotel/modelo/*.java hotel/negocio/*.java hotel/repositorio/*.java hotel/servico/*.java hotel/apresentacao/*.java

#- Executa o sistema
run:
	java -cp build hotel.Main

#- Executa os testes
test:
	java -cp build hotel.HotelTest

#- Limpa artefatos de compilacao
clean:
	rm -rf build/
