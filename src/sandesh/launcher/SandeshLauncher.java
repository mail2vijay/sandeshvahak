/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sandesh.launcher;

import sandesh.gui.GUIHandler;

/**
 *
 * @author victory
 */
public class SandeshLauncher {
    public static void main(String...s)
    {
        GUIHandler.getInstance().start();
    }
}
