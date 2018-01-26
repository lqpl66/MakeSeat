package com.yl.Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MP3player {
	private String filename;
	private Player player;

	public MP3player(String filename) {
	        this.filename = filename;
	    }

	public void play() {
		try {
			BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filename));
			player = new Player(buffer);
			player.play();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		MP3player mp3 = new MP3player("E:/林宥嘉 - 成全(Live).mp3");
		mp3.play();

	}

}
