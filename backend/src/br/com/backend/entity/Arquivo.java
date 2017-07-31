package br.com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Arquivo implements Serializable {
	
	/* ------------------------------
	 * CONSTANTES
	 * ------------------------------
	 */	
	private static final long serialVersionUID = 1L;
	
	/* ------------------------------
	 * ATRIBUTOS
	 * ------------------------------
	 */	
	private long id;
	
	private String nomeArquivo;
	
	private String nomeUsuario;
	
	private StatusUploadEnum statusUpload;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private long tempoEnvioMilisegundos;
	
	private int quantidadeBlocos;
	
	private byte[] conteudoArquivoBinario;
	
	/* ------------------------------
	 * GET'S AND SET'S
	 * ------------------------------
	 */	
	@ApiModelProperty(value = "corresponde ao numero do arquivo que foi inserido")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@ApiModelProperty(required = true,  value = "nome do arquivo inserido")
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	@ApiModelProperty(required = true,  value = "nome do usu�rio inserido")
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	@ApiModelProperty(value = "status do arquivo")
	public StatusUploadEnum getStatusUpload() {
		return statusUpload;
	}
	
	public void setStatusUpload(StatusUploadEnum statusUpload) {
		this.statusUpload = statusUpload;
	}
	
	
	@ApiModelProperty(value = "quantidade de blocos enviados")
	public int getQuantidadeBlocos() {
		return quantidadeBlocos;
	}

	@ApiModelProperty( value = "calcula o tempo gasto")
	public long getTempoEnvioMilisegundos() {
		return tempoEnvioMilisegundos;
	}

	public void setTempoEnvioMilisegundos(long tempoEnvioMilisegundos) {
		this.tempoEnvioMilisegundos = tempoEnvioMilisegundos;
	}

	@ApiModelProperty( value = "quantidade de blocos")
	public void setQuantidadeBlocos(int quantidadeBlocos) {
		this.quantidadeBlocos = quantidadeBlocos;
	}
	
	@ApiModelProperty(value = "data hora de inicio")
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	@ApiModelProperty(value = "data hora de fim")
	public Date getDataFim() {
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public byte[] getConteudoArquivoBinario() {
		return conteudoArquivoBinario;
	}
	public void setConteudoArquivoBinario(byte[] conteudoArquivoBinario) {
		this.conteudoArquivoBinario = conteudoArquivoBinario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeArquivo == null) ? 0 : nomeArquivo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arquivo other = (Arquivo) obj;
		if (nomeArquivo == null) {
			if (other.nomeArquivo != null)
				return false;
		} else if (!nomeArquivo.equals(other.nomeArquivo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Arquivo [nomeArquivo=" + nomeArquivo + "]";
	}

}
