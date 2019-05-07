package online.cszt0.cmt.util.plat;

import online.cszt0.cmt.util.FileDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * 基于 Java 原生实现的文件选择器
 *
 * @author 初始状态0
 * @date 2019/4/20 16:46
 */
public class FileDialogPlatCommon implements FileDialog {
	private JFileChooser fileDialog;

	public FileDialogPlatCommon() {
		fileDialog = new JFileChooser();
	}

	@Override
	public void addFileExtensionFilter(String description, String... extensions) {
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter(description, extensions));
	}

	@Override
	public String showOpenDialog() {
		int result = fileDialog.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		return fileDialog.getSelectedFile().getPath();
	}

	@Override
	public String showSaveDialog() {
		int result = fileDialog.showSaveDialog(null);
		if(result == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		return fileDialog.getSelectedFile().getPath();
	}

}
