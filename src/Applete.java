// ------------------------------------------------------------------
// Cluster_Applet.java
// ------------------------------------------------------------------

/*
 * ===============================================
 *
 * Copyright (c) 2006 Daniel Augusto F Collier.
 * ===============================================
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.JApplet;

/**
 * Usada para implementar o applet.
 *
 * Implementa os métodos  de controle do applet.
 *
 * @author Daniel Augusto F Collier
 * @author <a href="mailto:collier@linse.ufsc.br">collier@linse.ufsc.br</a>
 * @author <a target="_blank" href="http://www.linse.ufsc.br">www.linse.ufsc.br</a>
 * @version 1.0
 */

public class Applete extends JApplet implements ItemListener {
// ------------------------------------------------------------------
    private DrawDC myPanel = new DrawDC();
    
    private JScrollPane myScroll;
    private JScrollBar myBar;
    private JCheckBox cb;
    private JComboBox cmb;
    private JPanel barra;
    private GridLayout grid;
    
    private int x = 0, y = 0;
    
    private String[] status = { "0", "10", "20", "30", "40", "50", "60", "70", "80", "90"};
    private String[] statusP = { "0 %", "10 %", "20 %", "30 %", "40 %", "50 %", "60 %", "70 %", "80 %", "90 %"};    
    
    private boolean hasNodeSelected = false;
    private int lastNode = -1;
// ------------------------------------------------------------------
    /**
     * Implementa o applet.
     */      
    public void init() {
        try {
            myPanel.Init();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        myPanel.setBackground( Color.white );
        myPanel.setPreferredSize( new Dimension(5000, 5000) );
        
        myScroll = new JScrollPane( myPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        
        barra = new JPanel( new GridLayout(1,0) );
        
        cb = new JCheckBox( "Show Dead Node", false );
        cb.addItemListener( this );
     
        cmb = new JComboBox( statusP ); 
        cmb.addItemListener( 
                new ItemListener() {
                    public void itemStateChanged( ItemEvent event )
                    {
                            myPanel.nObj.cmb_filtro = Integer.valueOf(status[ cmb.getSelectedIndex() ]).intValue();
                            cb.setSelected( false );
                            cb.setSelected( false );
                            myPanel.nObj.ShowDead = false;
                            
                            myPanel.repaint();
                    }
                }
        );
        
        barra.add(cb);
        barra.add(cmb);
        barra.add( new JPanel() );
        
        add( myScroll, BorderLayout.CENTER );
        add( barra, BorderLayout.SOUTH );
        
        SetTimer();
        SetMouseEvent();
    }
// ------------------------------------------------------------------
    /**
     * Analisa a região da tela que recebeu o evento do mouse de clique, retornando 
     * o nó em que ocorreu o evento, ou -1 caso nenhum tenha sido clicado.
     *
     */     
    public void NodeClicked() {
        int yl;
        
        if ( x < 422 ) {
            yl = (int)  ( ( (float) y-22)/16  + 1 );
            
            x = -1;
            
            for ( int i=0; i<myPanel.nObj.NNO; i++ ) {
                int length = myPanel.nObj.area[i].length;
                
                for ( int j=0; j<length; j++ )
                    if (  yl == myPanel.nObj.area[i][j] ) {
                    x = i;
                    y = myPanel.nObj.area[i][j] - myPanel.nObj.area[i][0];
                    return;
                    }
            }
        }
    }
// ------------------------------------------------------------------
    /**
     * Trata o evento ocorrido no checkbox mudando a variável de estado ShowDead.
     * 
     */   
    public void itemStateChanged( ItemEvent e ) {
        myPanel.nObj.cmb_filtro = 0;
        
        if ( ( cmb.getSelectedIndex() ) != 0 && ( !myPanel.nObj.ShowDead ) ) {
            cmb.setSelectedIndex(0);
            cb.setSelected( true );
        }
        else {
            if ( myPanel.nObj.ShowDead )
                myPanel.nObj.ShowDead = false;
            else
                myPanel.nObj.ShowDead = true;
        }
        myPanel.repaint();
        }
// ------------------------------------------------------------------
    public void SetMouseEvent() {
        myPanel.addMouseListener(
                new MouseAdapter() {
            public void mouseClicked( MouseEvent event ) {
                if ( ( event.getButton() == event.BUTTON1 ) ) {
                    x = event.getX();
                    y = event.getY();
                    
                    int xC = x, yC = y;
                    NodeClicked();
                    
                     if ( ( xC < 422 ) && ( x != -1 ) ) {
                        if (xC > 14) {
                             if ( ( !myPanel.nObj.IsDead[x] && !myPanel.nObj.ShowDead) || myPanel.nObj.ShowDead ) { 
                                if ( !hasNodeSelected) {
                                    myPanel.nObj.IsSelected[ x ] = true;
                                    hasNodeSelected = true;
                                } else if ( x==lastNode ) {
                                    myPanel.nObj.IsSelected[ x ] = false;
                                    hasNodeSelected = false;
                                } else {
                                    myPanel.nObj.IsSelected[ lastNode ] = false;
                                    myPanel.nObj.IsSelected[ x ] = true;
                                    hasNodeSelected = true;
                                }
                                lastNode = x;
                            }
                            } else if ( ( x != -1 ) && ( xC < 14 ) ) {
                                if ( !myPanel.nObj.IsDead[ x ] )
                                    if ( myPanel.nObj.IsExpanded[ x ] ) {
                                    if ( y == 0 )
                                        myPanel.nObj.IsExpanded[ x ] = false;
                                    } else
                                        myPanel.nObj.IsExpanded[ x ] = true;
                        }
                    }                    
                    myPanel.repaint();
                }
            }
        } );
    }
// ------------------------------------------------------------------
    /**
     * Trata o evento de timer. A cada ciclo do timer ocorre um evento para atualização. 
     *
     */  
    public void SetTimer() {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {
                myPanel.repaint();
            }
        };
        new Timer( myPanel.nObj.TIMER_REFRESH, taskPerformer ).start();
    }
// ------------------------------------------------------------------
}
