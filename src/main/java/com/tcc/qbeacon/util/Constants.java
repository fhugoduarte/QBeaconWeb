package com.tcc.qbeacon.util;

public class Constants {
	
	public static final long TOKEN_EXPIRAR_MINUTOS = 24000 * 60000;
	
	public static final String URI_MQTT = "tcp://localhost:1883";
	public static final String TOPICO_MQTT_API = "qbeacon";
	public static final String TOPICO_MQTT_ARDUINO = "inTopic";
	
	/**
	 *  Contants de retornos da API
	 */
	public static final String ERRO_EMAIL_SENHA = "Senha e/ou Email incorretos!";
	public static final String SUCESSO_LOGIN_USUARIO = "Usuário logado com sucesso!";
	
	public static final String SUCESSO_CADASTRO_USUARIO = "Usuário cadastrado com Sucesso!";
	public static final String SUCESSO_ATUALIZAR_USUARIO = "Usuário atualizado com Sucesso!";
	public static final String SUCESSO_EXCLUIR_USUARIO = "Usuário excluído com Sucesso!";
	public static final String ERRO_CADASTRO_USUARIO = "Verifique sua conexão ou o Email informado !";
	
}
