package ui.Renderer;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import board.Status;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import multi.client.ui.MainFrame;
import ui.Assert;
import ui.FileExplorer;
import ui.util.Util;

public class AssetLoader {
	private FileExplorer main_folder, skines_folder, background_folder, bgm_folder;
//	private FileExplorer sound_folder;
//	private Team[] teams;
	
	public File check, checkmate;
	
	public AssetLoader(Team[]teams) {
		Assert.throwIF(teams==null, "loading dekinai");
		
		main_folder = new FileExplorer(Util.getJARdir(MainFrame.class)).goChild("PWG");
		skines_folder = main_folder.goChild("pieces");
		background_folder = main_folder.goChild("background");
		bgm_folder = main_folder.goChild("bgm");
//		sound_folder = explorer.goChild("sound");
		
		loadSkins(teams);
		loadBackgrounds();
		loadBGM();
	}
	
	private void loadBGM(){
		check = bgm_folder.loadUnivWAV("check");
		checkmate = bgm_folder.loadUnivWAV("checkmate");
	}
	
	private Map<String, SkinSet> skins = new HashMap<>();	
	private Map<String, BufferedImage> backgrounds = new HashMap<>();

	private void loadBackgrounds(){
		for(Status status : Status.values()){
			String name = status.getName();
			
			BufferedImage bgIMG = background_folder.laodUnivImage(name);
			if(bgIMG!=null) backgrounds.put(name, bgIMG);
		}
	}

	private void loadSkins(Team [] teams){		
		for(Team team: teams){
			FileExplorer skin = skines_folder.goChild(team.getName());
			SkinSet set = get(team+"");
			
			for(Pieces p: Pieces.values()){
				BufferedImage img = skin.laodUnivImage(p.getSymbol());
				set.register(p, img);
			
				SoundSet sound = loadSoundSet(skin.goChild(p+""));
				set.register(p, sound);
			}
			skins.put(skin.curDir().getName(), set);
		}
	}

	public SkinSet get(String skin){
		if(!skins.containsKey(skin)){
			skins.put(skin, new SkinSet());
		}
		return skins.get(skin);
	}
	
	private String[] default_sounds = {"place", "kill"};
	private SoundSet loadSoundSet(FileExplorer dir) {
		if(dir==null) return null;
		
		SoundSet set = new SoundSet();
		for(String name : default_sounds){
			File sound = dir.loadUnivWAV(name);
			set.register(name, sound);
		}
		return set;
	}

	public BufferedImage getBackground(String name){
		return backgrounds.get(name);
	}
	
	public BufferedImage getVisualOf(Pieces pclass, String skin){
		SkinSet set = skins.get(skin);
		
		return set==null? null: set.getVisual(pclass);
	}
	
	public File getSoundWhen(iPiece p, String state) {
		String skin = p.getTeam().getName();
		Pieces pclass = p.getPClass();
		
		SkinSet set = skins.get(skin);
//		System.out.println(set.getSoundSet(pclass).sounds.size());
		return set==null? null: set.getSoundSet(pclass).getSound(state);
	}

	public File getSoundWhen(Pieces pclass, String skin, String state){
		SkinSet set = skins.get(skin);
//		System.out.println(set.getSoundSet(pclass).sounds.size());
		return set==null? null: set.getSoundSet(pclass).getSound(state);
	}
	
	private class SoundSet{
		private HashMap<String, File> sounds = new HashMap<>();
		
		public void register(String name, File file){
			if(file!=null && file.exists()){
				sounds.put(name, file);
			}
		}
		
		public File getSound(String name){
			return sounds.get(name);
		}
	}
	
	private class SkinSet{
		private HashMap<Pieces, BufferedImage> visuals = new HashMap<>();
		private HashMap<Pieces, SoundSet> soundsets= new HashMap<>();
		
		public void register(Pieces pclass, BufferedImage visual){
			if(visuals!=null && pclass!=null){
				visuals.put(pclass, visual);
			}
		}
		
		public void register(Pieces pclass, SoundSet sound){
			if(sound!=null && pclass!=null){
				soundsets.put(pclass, sound);
			}
		}
		
		public BufferedImage getVisual(Pieces pclass){
			return visuals.get(pclass);
		}
		
		public SoundSet getSoundSet(Pieces pclass){
			return soundsets.get(pclass);
		}
	}
}

