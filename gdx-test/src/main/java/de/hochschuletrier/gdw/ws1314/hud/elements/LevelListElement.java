
package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LevelListElement extends TextButton {

	public LevelListElement(String text, Skin skin) {
		super(text, skin);
	}
	
	public String toString() {
		return super.getText().toString();
	}
}
/*
                                         __         __----__      
                                        /  \__..--''    _-__''-_  
                                       ( /  \            `-.''''` 
                                       | |   `-..__  .,     `.    
                         ___           ( '.  \ _____\ )`-_    `.  
                  ___   (   `.         '\   __/   __\' /-`:-.._ \ 
                 (   `-. `.   `.       .|\_  (   / .-| |W)|    ``'
                  `-.   `-.`.   `.     |' ( ,'\ ( (WW| \` j       
          ..---'''''-`.    `.\   _\   .|   ',  \_\_`/   ``-.      
        ,'            _`-,   `  (  |  |'     `.        \__/       
       /   _         ( ```    __ \  \ |     ._:7,______.-'        
      | .-'/          `-._   (  `.\  '':     \    /               
      '`  /          .-''>`-. `-. `   |       |  (                
         -          /   /    `_: `_:. `.    .  \  \               
         |          |  |  o()(   (      \   )\  ;  |              
        .'          `. |   Oo `---:.__-'') /  )/   |              
        |            | |  ()o            |/   '    |              
       .'            |/ \  o     /             \__/               
       |  ,         .|   |      /-,_______\       \               
      /  / )        |' _/      /     |    |\       \              
    .:.-' .'         )/       /     |     | `--,    \             
         /       .  / |      |      |     |   /      l            
    .__.'    ,   :|/_/|      |      |      | (       |            
    `-.___.-`;  / '   |      |      |      |  \      |            
           .:_-'      |       \     |       \  `.___/             
                       \_______)     \_______)
*/