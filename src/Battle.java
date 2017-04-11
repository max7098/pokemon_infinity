import java.awt.Graphics;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.awt.*;
public class Battle
{
    private int selectorBox;
    private int currentMenu;
    private boolean gameOn;
    private Pokemon computer;

    static BufferedImage fightScreen;

    public int getCurrentMenu()
    {
        return currentMenu;
    }
    public Battle()
    {
        gameOn = false;
        currentMenu = 0;
        selectorBox = 0;
        try
        {
            fightScreen = ImageIO.read(Pictures.load("battleScreen.png"));
        }
        catch (Exception e)
        {
            System.out.println("File not Found");
        }
    }

    public void endBattle()
    {
        gameOn = false;
    }

    public void casualEncounter()
    {
        WildPokemon madTemp = null;
        switch((int)(6*Math.random()))
        {
            case 0:
            madTemp = WildPokemon.Henry;
            break;
            case 1:
            madTemp = WildPokemon.Mark;
            break;
            case 2:
            madTemp = WildPokemon.Rakesh;
            break;
            case 3:
            madTemp = WildPokemon.Max;
            break;
            case 4:
            madTemp = WildPokemon.Ricky;
            break;
            case 5:
            madTemp = WildPokemon.Fisher;
        }

        gameOn = true;
        if(computer==null)
        {
            computer = new Pokemon(madTemp,2);
        }
    }

    public void updateCurentMenu(int i)
    {
        currentMenu = i;
    }

    public boolean getState()
    {
        return gameOn;
    }

    public void deSelected()
    {
        currentMenu = 0;
    }

    public void setSelectorBox(int x)
    {
        selectorBox = x;
    }

    public int getSelectorBox()
    {
        return selectorBox;
    }

    public void SelectorActivated(Pokemon playersFirst,Player player)
    {
        if(gameOn)
        {
            if(currentMenu==0)
            {
                if (selectorBox == 0)
                {
                    currentMenu = 1;
                }
                else if(selectorBox == 1)
                {
                    currentMenu = 2;
                }
                else if(selectorBox == 2)
                {
                    currentMenu = 3;
                }
                else if(selectorBox == 3)
                {
                    computer=null;
                    gameOn= false;
                    currentMenu = 0;
                    selectorBox = 0;
                }
            }
            else if(currentMenu == 1)
            {
                playersFirst.getMoves().get(selectorBox).use();
                playersFirst.healSelf(playersFirst.getMoves().get(selectorBox));
                computer.takeHit(playersFirst.getDamageDone(playersFirst.getMoves().get(selectorBox)));
                if(!computer.isAwake())
                {
                    computer = null;
                    gameOn=false;
                }
                currentMenu = 4;
            }
            else if(currentMenu == 2)
            {
                if(selectorBox==0)//catch them
                {
                    player.catchPokemon(computer);
                    computer=null;
                    gameOn= false;
                    currentMenu = 0;
                    selectorBox = 0;
                }
                else if (selectorBox==2)//heal
                {
                    playersFirst.fullHeal();
                    currentMenu=4;
                }
            }
            else if(currentMenu== 3)
            {
                if(player.getProGrammer(selectorBox).isAwake())
                {
                    player.swapGrammer(selectorBox);
                    currentMenu= 4;
                }
            }
            else if (currentMenu == 4)
            {
                CPUTurn(playersFirst);
                currentMenu= 0;
            }
        }
    }

    public void drawGame(Graphics g, Pokemon p,Player cha)
    {
        fighterLoaderSetUp(g,p);
        loadHealthBar(g,p);
        drawSelectorBox(g);
        if(currentMenu == 0)
        {
            startMenu(g);
        }
        else if(currentMenu == 1)
        {
            moveMenu(g,p);
        }
        else if(currentMenu == 2)
        {
            itemMenu(g);
        }
        else if(currentMenu == 3)
        {
            switchMenu(g, cha);
        }
        else
        {
            g.drawString("Opponent is making their move", 100, 366);
        }

    }

    public void switchMenu(Graphics g, Player p)
    {
        g.setColor(Color.BLACK);
        g.drawString(p.topRight(), 44, 326);
        g.drawString(p.bottomRight(), 44, 365);
        g.drawString(p.topLeft(), 243, 326);
        g.drawString(p.bottomLeft(), 243, 365);
    }

    public void drawSelectorBox(Graphics g)
    {
        g.setColor(Color.YELLOW);
        if(selectorBox==0)
        {
            g.fillRect(44, 308, 50,30);
        }
        else if(selectorBox==1)
        {
            g.fillRect(44, 347, 50,30);
        }
        else if(selectorBox==2)
        {
            g.fillRect(243, 308, 50,30);
        }
        else
        {
            g.fillRect(243, 347, 50,30);
        }
    }

    public void itemMenu(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.drawString("Idea", 44, 326);
        g.drawString("Caffeine", 243, 326);
    }

    public void moveMenu(Graphics g, Pokemon defender)
    {
        g.setColor(Color.BLACK);
        g.drawString(defender.getMoves().get(0).getName(), 44, 326);//top right
        g.drawString(defender.getMoves().get(1).getName(), 44, 365); //bottom right
        g.drawString(defender.getMoves().get(2).getName(), 243, 326);//top left
        g.drawString(defender.getMoves().get(3).getName(), 243, 365);//bottom left
    }

    public void startMenu(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.drawString("Move", 44, 326);
        g.drawString("Bag", 44, 365); 
        g.drawString("Switch" , 243, 326);
        g.drawString("Run", 243, 365);
    }

    public void fighterLoaderSetUp(Graphics g, Pokemon defender)
    {

        g.drawImage(fightScreen, 0, 0, null);
        g.drawImage(defender.getBack() , 54, 153, null);
        g.drawImage(computer.getFront() , 245, 36, null);
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(20f));

        g.setColor(Color.BLACK);
        g.drawString(computer.getName(), 45, 73);//displays the name
        g.drawRect(45, 73+10,64,6);//border of status bar
        g.drawString(defender.getName(), 254, 240);//displays the name
        g.drawRect(254, 240+10,64,6);
    }

    public void fighterLoaderLite(Graphics g,Pokemon defender)//draws everything but the fighters
    {
        g.drawImage(fightScreen, 0, 0, null);
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(20f));

        g.setColor(Color.BLACK);
        g.drawString(computer.getName(), 45, 73);//displays the name
        g.drawRect(45, 73+10,64,6);//border of status bar
        g.drawString(defender.getName(), 254, 240);//displays the name
        g.drawRect(254, 240+10,64,6);


    }

    public void loadHealthBar(Graphics g, Pokemon defender)
    {
        if((int)(64*computer.getHealthPercent()) >= 32)
        {
            g.setColor(Color.GREEN);
        }
        if((int)(64*computer.getHealthPercent()) < 32)
        {
            g.setColor(Color.ORANGE);
        }
        if((int)(64*computer.getHealthPercent()) < 16)
        {
            g.setColor(Color.RED);
        }

        //g.setColor(Color.ORANGE);
        g.fillRect(46,74 + 10, (int)(64*computer.getHealthPercent()) ,4);

        if((int)(64*defender.getHealthPercent()) >= 32)
        {
            g.setColor(Color.GREEN);
        }
        if((int)(64*defender.getHealthPercent()) < 32)
        {
            g.setColor(Color.ORANGE);
        }
        if((int)(64*defender.getHealthPercent()) < 16)
        {
            g.setColor(Color.RED);
        }
        g.fillRect(255, 241 + 10, (int)(64*defender.getHealthPercent()), 4);
        //Control which statments to draw with if statements

    }

    public void CPUTurn(Pokemon defender)
    {
        computer.getMoves().get((int)(Math.random()*4)).use();
        computer.healSelf(computer.getMoves().get((int)(Math.random()*4)));
        defender.takeHit(computer.getDamageDone(computer.getMoves().get((int)(Math.random()*4))));
    }

    public Pokemon getComputer()
    {
        return computer;
    }

}