package br.com.backend.entity;

import com.google.gson.annotations.SerializedName;

public enum StatusUploadEnum {
	
	@SerializedName("Em andamento")
	EM_ANDAMENTO("Em andamento"), 
	@SerializedName("Falha")
	FALHA("Falha"), 
	@SerializedName("Conclu�do")
	CONCLUIDO("Conclu�do");

	public String descricao;
	
	StatusUploadEnum(String descricaoEnum) {
		descricao = descricaoEnum;
	}
}
