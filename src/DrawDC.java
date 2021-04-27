// ------------------------------------------------------------------
// DrawDC.java
// ------------------------------------------------------------------

/*
 * ===============================================
 *
 * Copyright (c) 2006 Daniel Augusto F Collier. 
 * ===============================================
 */

import java.awt.*;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.*;

/**
 * Desenha sobre o painel.
 *
 * @author Daniel Augusto F Collier
 * @author <a href="mailto:collier@linse.ufsc.br">collier@linse.ufsc.br</a>
 * @author <a target="_blank" href="http://www.linse.ufsc.br">www.linse.ufsc.br</a>
 * @version 1.0
 */

public class DrawDC extends JPanel {
// ------------------------------------------------------------------
    /**
     * Objeto com as informações dos nós armazenadas em suas variáveis.
     * @see NodeObject
     */
    public NodeObject nObj;
    
    private Color BGCOLOR = new Color(0xF0, 0xF0, 0xF0);
    private Color BGBARCOLOR = new Color(0xF5, 0xF5, 0xF5);
    private Color DEADTEXTCOLOR = new Color(0xB0, 0xB0, 0xB0);
    private Color TEXTCOLOR = new Color(0, 0, 0);
    private Color SELBGCOLOR = new Color(0x0D, 0x0D, 0xBF);
    private Color SELTEXTCOLOR = new Color(0xFF, 0xFF, 0xFF);
    
    private Font font_bold = new Font( "Monospaced", Font.BOLD, 10 );
    private Font font_normal = new Font( "Monospaced", Font.PLAIN, 10 );
       
    private int LINE_SIZE = 16;
    private int charWidth;
    private int ypos;
    private int NCpus;
    private int line;
    
    /**
     * Número máximo de nós exibidos.
     */
    public int NNO;
    /**
     * Número máximo de cpus exibidas.
     */
     public int NCPU;
// ------------------------------------------------------------------
/**
 * Inicialização da classe: leitura do arquivo de configuração e 
 * inicialização de um objeto do tipo NodeObject.
 *
 */    
    public void Init() throws IOException {
        Config config = new Config();
        NNO = config.NNO;
        NCPU = config.NCPU;
        nObj = new NodeObject( config );
    }
// ------------------------------------------------------------------
/**
 * Executa as chamadas aos métodos de desenho, desenhando toda 
 * a informação contida nos nós solicitada.
 *
 */
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        
        nObj.GetInfo();
        
        DrawHeader( g );
        
        for ( int i=0; i<NNO; i++ )
            DrawNode( g, nObj, i);
        
        g.setColor( Color.black );
        g.setFont( font_bold );
        
        /*
        // Pegar todos os nomes das fontes do seu sistema.
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontNames[] = ge.getAvailableFontFamilyNames();
         
        // Iterate the font family names
        for (int i=0; i<fontNames.length; i++)
            System.out.println("\n" + fontNames[i]);
         */
    }
// ------------------------------------------------------------------
/**
 * Desenha o cabeçalho.
 *
 */
    public void DrawHeader( Graphics g ) {
        g.setColor( BGCOLOR );
        g.fillRect( 2, 2, 422, 20 );
        
        g.setColor( Color.black );
        g.setFont( font_bold );
        
        g.drawString( "Name", 20, 16 );
        g.drawString( "Load", 80, 16 );
        
        g.setFont( font_normal );
        FontMetrics metrics = g.getFontMetrics();
        charWidth = metrics.getWidths()[0];
        
        g.drawString( "0 %", 120, 16);
        g.drawString( "50 %", (420-150-4*charWidth), 16 );
        g.drawString( "100 %", (420-5*charWidth), 16 );
    }
// ------------------------------------------------------------------
/**
 * Desenha o nó e suas extensões.
 *
 */   
    public void DrawNode( Graphics g, NodeObject nObj, int nno ) {
        g.setFont( font_normal );
        FontMetrics metrics = g.getFontMetrics();
        charWidth = metrics.getWidths()[0];
        
        line = nObj.line[nno];
        ypos = LINE_SIZE*line + 6;
        NCpus = nObj.strStatus[nno].length -1;
        
        if ( !nObj.IsDead[nno] ) {
            g.setColor( nObj.IsSelected[nno] ? SELBGCOLOR : BGCOLOR );
            g.fillRect( 2, ypos, 422, LINE_SIZE );
            
            // desenha a porcentagem em andamento.
            if ( nObj.NodeStatus[nno][0]!=100 ) {
                
                if ( nObj.NodeStatus[nno][0] < 10 ) {
                    g.setColor(nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR);
                    g.drawString( " " + Integer.toString(nObj.NodeStatus[nno][0])
                    +" %", 120-charWidth*7, ypos+12 );
                } else {
                    g.setColor(nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR);
                    g.drawString( Integer.toString(nObj.NodeStatus[nno][0])
                    + " %", 120-charWidth*7, ypos+12 );
                }
            } else {
                g.setColor( nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR );
                g.drawString( " " + Integer.toString(nObj.NodeStatus[nno][0])
                + " %", 120-charWidth*9, ypos+12 );
            }
            // desenha o nome do nó
            g.setColor( nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR );
            g.drawString( nObj.nNames[nno], 20, ypos+12 );
            
            // desenha a barra de percetagem
            nObj.LoadBar.paintIcon(this, g, 120, ypos+2);
            g.setColor( BGBARCOLOR );
            g.fillRect( 120+3*nObj.NodeStatus[nno][0], ypos+2,
                    300-3*nObj.NodeStatus[nno][0], LINE_SIZE-4 );
            
            g.setColor( BGCOLOR );
            g.fillRect( 2, ypos, LINE_SIZE, LINE_SIZE );
            
        } else {
            if ( nObj.ShowDead ) {
                // desenha nome de nó dado como "DEAD"
                g.setColor( nObj.IsSelected[nno] ? SELBGCOLOR : BGCOLOR );
                g.fillRect( 2, ypos, 422, LINE_SIZE );
                
                g.setColor( DEADTEXTCOLOR );
                g.drawString( nObj.nNames[nno], 20, ypos+12 );
                g.drawString( "D E A D", 120, ypos+12 );
                
                g.setColor( BGCOLOR );
                g.fillRect( 2, ypos, LINE_SIZE, LINE_SIZE );                
            }
        }
        
        if ( !nObj.IsDead[nno] ) {
            // Área destinada para o "botão" de expansão do nó
            g.setColor( nObj.IsSelected[nno] ? SELBGCOLOR : TEXTCOLOR );
            
            if  ( nObj.IsSelected[nno])
                g.fillRect( 3, ypos+2, 11, 9 );
            else
                g.drawRect( 3, ypos+1, 10, 10 );
            
            g.setColor( nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR);
            g.drawLine( 5, ypos+6, 11, ypos+6);
            
            if ( nObj.IsExpanded[nno] ) {
                // desenha as partes expandidas
                for ( int k=1; k<=NCpus; k++ ) {
                    g.setColor( nObj.IsSelected[nno] ? SELBGCOLOR : BGCOLOR );
                    g.fillRect( 2, ypos+LINE_SIZE*k, 422, LINE_SIZE );
                }
                
                // desenha linha expandida
                for ( int k=1; k<=NCpus; k++ ) {
                    // desenha campo a esquerda dos nomes do nós
                    g.setColor( BGCOLOR );
                    g.fillRect( 2, ypos+LINE_SIZE*k, LINE_SIZE, LINE_SIZE );
                    
                    // desenha campo dos nomes das Cpus
                    g.setColor(nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR);
                    g.drawString( "CPU " + Integer.toString(k-1), 20,
                            ypos+LINE_SIZE*k+12 );
                    
                    // desenha parte carregada da barra
                    nObj.LoadBar.paintIcon(this, g, 120, ypos+LINE_SIZE*k+2);
                    
                    g.setColor( BGBARCOLOR );
                    g.fillRect( 120+3*nObj.NodeStatus[nno][k], ypos+LINE_SIZE*k
                            +2, 300-3*nObj.NodeStatus[nno][k], LINE_SIZE-4 );
                }
                
                g.setColor( nObj.IsSelected[nno] ? SELBGCOLOR : TEXTCOLOR );
                
                // desenha os pontos verticais na expansão
                for ( int i=1; i<8*NCpus; i++ )
                    g.drawString( ".", 7, ypos+10+i*2);
                
                // desenha os pontos horizontais na expansão
                for ( int i=1; i<=3; i++)
                    for ( int j=1; j<=NCpus; j++)
                        g.drawString(".", 7+i*2, ypos+8+16*j );
            } else {
                // desenha o traço vertical na caixa de expansão
                g.setColor( nObj.IsSelected[nno] ? SELTEXTCOLOR : TEXTCOLOR );
                g.drawLine(8, ypos+3, 8, ypos+9);
            }
        }
    }
// ------------------------------------------------------------------
}

