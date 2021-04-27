// ------------------------------------------------------------------
// ClientX.java
// ------------------------------------------------------------------

/*
 * ===============================================
 *
 * Copyright (c) 2006 Daniel Augusto F Collier.
 * ===============================================
 */

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Utilizada para processar a informação proviniente do cluster.
 *
 * @author Daniel Augusto F Collier
 * @author <a href="mailto:collier@linse.ufsc.br">collier@linse.ufsc.br</a>
 * @author <a target="_blank" href="http://www.linse.ufsc.br">www.linse.ufsc.br</a>
 * @version 1.0
 */

public class ClientX {
// ------------------------------------------------------------------
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket socket;
    
    private int TIME_OUT;
    private int BYTES;
    private int port=-1;
    
    private Properties CpuLoads = new Properties();
// ------------------------------------------------------------------
    /**
     * Processa os métodos da classe retornando o dicionário
     * de chaves e valores.
     *
     * @param BYTES tamanho do vetor em que será 
     * armezenada a resposta do servidor
     * @param TIME_OUT tempo de espera da resposta 
     * do servidor
     * @return CpuLoads um dicionário em que as chaves
     * são o nomes do nós e os valores os vetores com 
     * o status do cluster
     */
     public Properties ClientX( int BYTES, int TIME_OUT ) {
        CpuLoads.clear();  // um dicionário novo
        
        /* 
         * Caso não exista socket aberto tentasse abrir um.
         */
        if ( port == -1 ) {
            try {
                socket.close();
            } catch ( RuntimeException error) {}
            port = GetListenSocket( TIME_OUT );     
        }
        /*
         * Resultado do processo de obtenção da informação do cluster.
         */
        if ( port != -1 ) {
            SendMessage();
            RecvSocket( BYTES );
        } else
            CpuLoads.setProperty("","");
         
        return CpuLoads;
    }
// ------------------------------------------------------------------
    /**
     * Tenta abrir um socket para escutar a mensagem 
     * do servidor. 
     *
     * @param TIME_OUT tempo de espera da resposta 
     * do servidor
     * @return a porta onde vai ficar o socket que irá 
     * escutar a mensagem do servidor
     */    
    public int GetListenSocket( int TIME_OUT ) {
        for ( port=8001; port<8099; port++ ) {
            try {
                socket = new DatagramSocket( port );
                socket.setSoTimeout( TIME_OUT );
                break;
            } catch ( SocketException socketException ) { }
        }
        return port;
    }
// ------------------------------------------------------------------
    /**
     * Tenta enviar a menssagem para o servidor. 
     *
     */    
    public void SendMessage() {
        try {
            String message = "<Query port=\"" + Integer.toString(port)
            + "\">CpuLoads()</Query>";
            
            byte data[] = message.getBytes();
            
            sendPacket = new DatagramPacket( data, data.length,
                    InetAddress.getByName( "192.168.2.21" ), 8000);
            
            socket.send( sendPacket );
        } catch ( IOException ioException ) {
            System.out.println("erro");
        }
    }
// ------------------------------------------------------------------
    /**
     * Tenta receber a mensagem do servidor.
     *
     * @param BYTES tamanho do vetor em que será 
     * armezenada a resposta do servidor
     * @throws cria dicionário vazio e pelo parâmetro 
     * port tenta alocar outro socket para escutar
     */    
    public void RecvSocket( int BYTES ) {
        try {
            byte data[] = new byte[ BYTES ];
            receivePacket = new DatagramPacket( data, data.length );
            socket.receive( receivePacket );
            GetPackets();
        }
        
        catch( IOException exception ) {
            CpuLoads.setProperty("","");
            port = -1;
        }
    }
// ------------------------------------------------------------------
    /**
     * Organiza mensagem recebida no dicionário de 
     * chaves e valores.
     *
     */   
    public void GetPackets() {
        String message;
        String array[];
        String array2[];
        
        message = new String( receivePacket.getData(), 0,
                receivePacket.getLength() );
        if ( message.compareTo( "" ) == 0 ) {
            CpuLoads.setProperty("","");
            port = -1;
        }
        else {
            message = message.substring( message.indexOf( "(" ) + 1,
                    message.lastIndexOf(")") );
            array = message.split( "," );
        
            for ( int i=0; i < array.length; i++ ) {
                array2 = array[i].split( "=" );
                CpuLoads.setProperty(array2[0], array2[1]);
            }
        }
    }
// ------------------------------------------------------------------
}


