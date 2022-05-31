package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class LoadDialog {

    private JDialog dialog;

    LoadDialog(CollecteurEvenements controle, Vector<String> savesNames){
        dialog = new JDialog();

        dialog.setBounds(500, 300, 300, 150);

        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();

        JLabel label = new JLabel("Selectionner un sauvegarde :");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        horizontalBox.add(label);
        horizontalBox.add(Box.createGlue());

        JComboBox combo = new JComboBox(savesNames);
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        horizontalBox.add(combo);

        verticalBox.add(horizontalBox);
        verticalBox.add(Box.createGlue());

        JButton confirm = new JButton("Charger la sauvegarde");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Commande com = new Commande("load");
                if(combo.getSelectedItem() != null && !combo.getSelectedItem().toString().equals("")) {
                    com.setSaveName(combo.getSelectedItem().toString() + ".TTYKM");
                    controle.commande(com);
                }
                dialog.dispose();
            }
        });
        verticalBox.add(confirm);

        dialog.add(verticalBox);
        dialog.setVisible(true);
    }
}
