/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

import BD.ConexionBD;
import Controladores.RecordatorioControlador;
import Modelos.Recordatorio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 *
 * @author Lenovo Ideapad
 */
public class PanelListaRecordatorios extends javax.swing.JPanel {

    /**
     * Creates new form PanelListaRecordatorios
     */
    private RecordatorioControlador controlador;
    private String usuario;
    private Timer refreshTimer;
    private int tiempoAc=5000;
    public PanelListaRecordatorios() {
        initComponents();
    }

    public PanelListaRecordatorios(RecordatorioControlador controlador, String usuario) {
        this.controlador = controlador;
        this.usuario = usuario;
        initComponents();
        
        jPanelMain.setLayout(new BorderLayout());
        jPanelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        recordatoriosContainer.setLayout(new BoxLayout(recordatoriosContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(recordatoriosContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPanelMain.add(scrollPane, BorderLayout.CENTER);

        // Cargar recordatorios iniciales
        cargarRecordatorios();

        // Configurar timer para actualizar cada 30 segundos
        refreshTimer = new Timer(tiempoAc, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarRecordatorios();
            }
        });
        refreshTimer.start();
    }

    private void cargarRecordatorios() {
        // Limpiar panel de recordatorios
        recordatoriosContainer.removeAll();

        // Cargar recordatorios desde el controlador
        controlador.cargarRecordatorios();
        List<Recordatorio> recordatorios = controlador.recordatorios;

        if (recordatorios.isEmpty()) {
            JLabel emptyLabel = new JLabel("No hay recordatorios activos");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            recordatoriosContainer.add(emptyLabel);
        } else {
            for (Recordatorio recordatorio : recordatorios) {
                recordatoriosContainer.add(crearPanelRecordatorio(recordatorio));
                // Agregar un pequeño espacio entre recordatorios
                recordatoriosContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // Actualizar UI
        recordatoriosContainer.revalidate();
        recordatoriosContainer.repaint();
    }

    private JPanel crearPanelRecordatorio(Recordatorio recordatorio) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Calcular proximidad del evento para determinar el color
        Color colorFondo = calcularColorProximidad(recordatorio.getFechaEvento(), recordatorio.getHoraEvento());
        panel.setBackground(colorFondo);

        // Panel para información del recordatorio
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Título del evento
        JLabel tituloLabel = new JLabel(recordatorio.getTituloEvento());
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(tituloLabel);

        // Fecha y hora
        String fechaHora = String.format("Fecha: %tF - Hora: %tR",
                recordatorio.getFechaEvento(), recordatorio.getHoraEvento());
        JLabel fechaLabel = new JLabel(fechaHora);
        infoPanel.add(fechaLabel);

        // Descripción
        JLabel descLabel = new JLabel(recordatorio.getDescripcionEvento());
        descLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPanel.add(descLabel);

        // Tiempo restante hasta el evento
        long tiempoEvento = recordatorio.getFechaEvento().getTime();
        if (recordatorio.getHoraEvento() != null) {
            tiempoEvento += recordatorio.getHoraEvento().getTime() % (24 * 60 * 60 * 1000);
        }
        long ahora = System.currentTimeMillis()+18000000;
        long diferencia = tiempoEvento - ahora;
        long minutosFaltantes = TimeUnit.MILLISECONDS.toMinutes(diferencia);
        long horasFaltantes = minutosFaltantes / 60;
        minutosFaltantes = minutosFaltantes % 60;

        String tiempoRestante;
        if (horasFaltantes > 24) {
            long diasFaltantes = horasFaltantes / 24;
            tiempoRestante = String.format("Tiempo restante: %d días, %d horas, %d minutos",
                    diasFaltantes, horasFaltantes % 24, minutosFaltantes);
        } else if (horasFaltantes > 0) {
            tiempoRestante = String.format("Tiempo restante: %d horas, %d minutos",
                    horasFaltantes, minutosFaltantes);
        } else {
            tiempoRestante = String.format("Tiempo restante: %d minutos", minutosFaltantes);
        }

        JLabel tiempoRestanteLabel = new JLabel(tiempoRestante);
        tiempoRestanteLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(tiempoRestanteLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Checkbox para marcar como visto
        JCheckBox vistoCheck = new JCheckBox("Visto");
        vistoCheck.setOpaque(false);
        vistoCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vistoCheck.isSelected()) {
                    try {
                        // Eliminar recordatorio
                        boolean eliminado = controlador.eliminarRecordatorio(recordatorio.getId());
                        if (eliminado) {
                            // Hacer que el panel desaparezca con animación
                            Timer timer = new Timer(50, new ActionListener() {
                                float alpha = 1.0f;

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    alpha -= 0.1f;
                                    if (alpha <= 0) {
                                        ((Timer) e.getSource()).stop();
                                        cargarRecordatorios(); // Recargar todos los recordatorios
                                    } else {
                                        panel.setBackground(new Color(
                                                colorFondo.getRed(),
                                                colorFondo.getGreen(),
                                                colorFondo.getBlue(),
                                                (int) (alpha * 255)
                                        ));
                                        panel.repaint();
                                    }
                                }
                            });
                            timer.start();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                PanelListaRecordatorios.this,
                                "Error al eliminar el recordatorio: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        panel.add(vistoCheck, BorderLayout.EAST);

        // Botón adicional (sin funcionalidad, para ser programado por el usuario)
        

        return panel;
    }

    private Color calcularColorProximidad(Date fechaEvento, Date horaEvento) {
        // Combinar fecha y hora del evento
        long tiempoEvento = fechaEvento.getTime();
        if (horaEvento != null) {
            tiempoEvento += horaEvento.getTime() % (24 * 60 * 60 * 1000);
        }

        // Calcular diferencia en minutos
        long ahora = System.currentTimeMillis()+18000000;
        long diferencia = tiempoEvento - ahora;
        long minutosFaltantes = TimeUnit.MILLISECONDS.toMinutes(diferencia);

        // Escala de colores: verde (lejos) -> amarillo -> naranja -> rojo (cerca)
        if (minutosFaltantes <= 30) {
            return new Color(255, 50, 50); // Rojo intenso
        } else if (minutosFaltantes <= 60) {
            return new Color(255, 100, 50); // Rojo-naranja
        } else if (minutosFaltantes <= 120) {
            return new Color(255, 150, 50); // Naranja
        } else if (minutosFaltantes <= 240) {
            return new Color(255, 200, 50); // Amarillo-naranja
        } else if (minutosFaltantes <= 480) {
            return new Color(255, 255, 100); // Amarillo
        } else {
            return new Color(220, 255, 220); // Verde claro
        }
    }

    // Método para detener el timer cuando el panel ya no es visible
    public void detenerActualizaciones() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    // Método para reanudar actualizaciones
    public void reanudarActualizaciones() {
        if (refreshTimer != null && !refreshTimer.isRunning()) {
            refreshTimer.start();
        }
    }

 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanelMain = new javax.swing.JPanel();
        recordatoriosContainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jButton1.setText("Añadir Recordatorio");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout recordatoriosContainerLayout = new javax.swing.GroupLayout(recordatoriosContainer);
        recordatoriosContainer.setLayout(recordatoriosContainerLayout);
        recordatoriosContainerLayout.setHorizontalGroup(
            recordatoriosContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        recordatoriosContainerLayout.setVerticalGroup(
            recordatoriosContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );

        jLabel1.setText("Recordatorios Activos");

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(recordatoriosContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGap(226, 226, 226)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(recordatoriosContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(209, 209, 209)
                .addComponent(jButton1)
                .addContainerGap(226, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       new BuscarEventoVista1(usuario).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel recordatoriosContainer;
    // End of variables declaration//GEN-END:variables
}
