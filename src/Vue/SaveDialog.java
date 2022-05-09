package Vue;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveDialog {

    private JDialog dialog;

    SaveDialog(CollecteurEvenements controle){
        dialog = new JDialog();

        dialog.setBounds(500, 300, 300, 100);

        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();

        JLabel label = new JLabel("Nom de la sauvegarde : ");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        horizontalBox.add(label);
        horizontalBox.add(Box.createGlue());

        JTextField text = new JTextField();
        horizontalBox.add(text);

        verticalBox.add(horizontalBox);
        verticalBox.add(Box.createGlue());

        JButton confirm = new JButton("Sauvegarder ma partie");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.getText().isBlank()){
                    text.setBorder(new LineBorder(Color.RED, 2));
                }else{
                    Commande com = new Commande("save");
                    com.setSaveName(text.getText().trim()+".sav");
                    controle.commande(com);
                    dialog.dispose();
                }
            }
        });
        verticalBox.add(confirm);

        dialog.add(verticalBox);
        dialog.setVisible(true);
    }

}
