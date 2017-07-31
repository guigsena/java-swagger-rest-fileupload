package br.com.backend.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import br.com.backend.entity.Arquivo;
import br.com.backend.entity.StatusUploadEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@Path("/arquivo")
public class ArquivoResource {

	/* ------------------------------
	 * CONSTANTES
	 * ------------------------------
	 */	
	
	public static List<Arquivo> lstArquivos = new ArrayList<Arquivo>();//Lista singleton para armazenar os arquivos
	private static final Logger LOGGER = Logger.getLogger( ArquivoResource.class.getName() );
	private Arquivo arquivoSelecionado;
	private static HashMap<Integer, byte[]> lstPacotes = new HashMap<Integer, byte[]>();
	
	/* ------------------------------
	 * MÉTODOS
	 * ------------------------------
	 */	
	
	
		
	/**
	 * @author Guilherme Sena
	 * @param idArquivo
	 * @return
	 */
	@GET
	@Path("/download/{idarquivo:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@ApiOperation(value = "Realiza download de arquivo na memória", response = StreamingOutput.class)
	@ApiResponses( value = {
		    @ApiResponse(code = 200, message = "Download realizado com sucesso", response = StreamingOutput.class),
		    @ApiResponse(code = 500, message = "Erro interno")}	)
	public Response download(@PathParam("idarquivo") long idArquivo) {
		arquivoSelecionado = new Arquivo();
		for (Arquivo arquivo : lstArquivos) {
			if(arquivo.getId() == idArquivo){
				arquivoSelecionado = arquivo;        				
			}
		}
		
		StreamingOutput fileStream =  new StreamingOutput() 
        {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException 
            {
	            byte[] data = arquivoSelecionado.getConteudoArquivoBinario();
	            output.write(data);
	            output.flush();
            }
        };        
        return Response.ok(fileStream).header("content-disposition","attachment; filename = " + arquivoSelecionado.getNomeArquivo() ).build();
        
	}
	
	/**
	 * @author Guilherme Sena
	 * @return lista de arquivos
	 */
	@GET
	@Path("/listar-todos-arquivos")
	@ApiOperation(value = "Returns lista de arquivos inseridos", notes = "exibe todos os dados do arquivo", response = Arquivo.class)
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successful retrieval of user detail", response = Arquivo.class),
	    @ApiResponse(code = 500, message = "Internal server error")}
	)
	public Response listarTodosArquivos() {
		Gson gson = new Gson();
		String retorno = null;
		try {
			retorno = gson.toJson(lstArquivos);
		} catch (Exception e) {
			LOGGER.log( Level.SEVERE, e.toString(), e );
		}
		//lstArquivos
		return Response.status(200).entity(retorno).build();
	}
	
	/**
	 * @author Guilherme Sena 
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param numPacote
	 * @param totalPacotes
	 * @param fileSize
	 * @param nomeUsuario
	 * @return
	 */
	  @POST 
	  @Path("/upload")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)
	  @ApiOperation(value = "Envia arquivo e armazena na memória")
		@ApiResponses( value = {
			    @ApiResponse(code = 200, message = "Upload realizado com sucesso"),
			    @ApiResponse(code = 400, message = "Arquivo já inserido"),
			    @ApiResponse(code = 500, message = "Erro interno")}	)
	  public Response uploadFile(
			  @FormDataParam("file")InputStream uploadedInputStream,
			  @FormDataParam("nomeArquivo") FormDataContentDisposition fileDetail,
			  @FormDataParam("numPacote") Integer numPacote,
			  @FormDataParam("totalPacotes") Integer totalPacotes,
			  @FormDataParam("fileSize") Integer fileSize,
			  @FormDataParam("nomeUsuario") String nomeUsuario
			  ){
		  
		  Arquivo arquivo = new Arquivo();
		  boolean existeArquivo = false;
		  
		    try {
		    	arquivo.setNomeArquivo(fileDetail.getFileName());
				if (!lstArquivos.contains(arquivo)) {
					arquivo.setStatusUpload(StatusUploadEnum.EM_ANDAMENTO);
			  		  arquivo.setQuantidadeBlocos(lstPacotes.size());
			  		  arquivo.setId(lstArquivos.size() + 1);
			  		  arquivo.setNomeUsuario(nomeUsuario);
			  		  arquivo.setDataInicio(new Date());
			          lstArquivos.add(arquivo);
		        } else if(numPacote == 1){
		        	existeArquivo = true;
		        }
				
				//chunk
		  	  byte[] pacote = IOUtils.toByteArray(uploadedInputStream);
		  	  //salva pacote na lista
		  	  lstPacotes.put(numPacote, pacote);

		      if (lstPacotes.size() == totalPacotes) {
		    	  if(existeArquivo){
		    		  return Response.status(400).build();
		    	  }
		    	  
		        arquivo = (Arquivo)lstArquivos.get(lstArquivos.indexOf(arquivo));
		        arquivo.setQuantidadeBlocos(lstPacotes.size());
		        arquivo.setConteudoArquivoBinario(montaArquivoBinario(uploadedInputStream,fileSize));
		        arquivo.setStatusUpload(StatusUploadEnum.CONCLUIDO);
		        arquivo.setDataFim(new Date());
		        arquivo.setTempoEnvioMilisegundos(arquivo.getDataFim() == null ? new Date().getTime() : arquivo.getDataFim().getTime() - arquivo.getDataInicio().getTime());
		        
		      }
		      
		    } catch (IOException e) {
		      arquivo.setStatusUpload(StatusUploadEnum.FALHA);
		      arquivo.setDataFim(new Date());
		      LOGGER.log(Level.SEVERE, e.toString(), e);
		      lstPacotes = new HashMap<Integer, byte[]>();
		      return Response.status(500).build();
		    }
		    

		    return Response.ok().build();
		  

	  } 
	  
	  /**
	   * @Guilherme Sena
	   * @param uploadedInputStream
	   * @param fileSize
	   * @param numPacote
	   * @param totalPacotes
	   * @return arquivo em formato binário
	   * @throws IOException
	   */
	  private byte[] montaArquivoBinario(InputStream uploadedInputStream, Integer fileSize) throws IOException {
		  //Arquivo em bytes
		  byte[] arquivoBytes = new byte[fileSize];

			  //ordena pelo numero do pacote que pode vir em ordem aleatoria
			  Map<Integer, byte[]> treeMap = new TreeMap<Integer, byte[]>(lstPacotes);
			  ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			  //escreve cada pacote
			  for(Map.Entry<Integer, byte[]> entry : treeMap.entrySet()) {
				  outputStream.write( entry.getValue() );
			  }
			  //adiciona conteudo total em bytes
			  arquivoBytes = outputStream.toByteArray();
			  //limpa variavel
			  lstPacotes = new HashMap<Integer, byte[]>();
		  
		  return arquivoBytes; 
	  }
	
}
