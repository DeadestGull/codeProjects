import java.awt.*;  
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
public class game {
	
	public static void main(String args[]){
		gui g=new gui();
		g.start();
	}
}
class laser{
	private int x;
	private int y;
	public laser(int x, int y){
		this.x=x;
		this.y=y;	
	}	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void move(){
		y-=10;
	}
}
class coin{
	private int x;
	private int y;
	private boolean alive=true;
	public coin(int x, int y){
		this.x=x;
		this.y=y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void pickUp(){
		alive=false;
	}
	public boolean getStatus(){
		return alive;
	}
}
class platform{
	private int x;
	private int y;
	private int width;
	private String type;
	private ArrayList<coin> c=new ArrayList<coin>();
	public platform(int x, int y, int size, String type){
		this.type=type;
		this.x=x;
		this.y=y;
		width=size;
		if (type.equals("coin")){
			for (int i=0; i<width/33; i++)
				c.add(new coin(x+i*33+16,y-20));
		}
	}
	public ArrayList<coin> getCoin(){
		return c;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getW(){
		return width;
	}
	public String getT(){
		return type;
	}
}
class player {
	private int x;
	private int y;
	private int jump;
	private int jumpMax=1;
	private Color color;
	private int coins=0;
	private int coinMulti=1;
	private ArrayList<platform> p;
	public player(Color color){
		x=900;
		y=290;
		jump=jumpMax;
		this.color=color;
	}
	public void upgradeCoin(){
		coinMulti++;
	}
	public void reset(){
		x=900;
		y=289;
		jump=jumpMax;
	}
	public String getYShow(){
		return ""+(0-y+290)/100.0;
	}
	public void getPlat(ArrayList plat){
		p=plat;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Color getColor(){
		return color;
	}
	public void moveRight(double a){
		int beforeX=x;
		x+=(int)a+1;
		if (x>1775)
			x=1775;
		
		for (platform q:p){
			if (y-q.getY()+50>300)
					break;
			if (beforeX+10<=q.getX()&&x+10>=q.getX()&&y+15>q.getY()&&y-15<q.getY()){
				jump=jumpMax;
				x=q.getX()-10;
			}
		}
	}
	public void loseCoins(int a){
		coins-=a;
	}
	public void moveLeft(double a){
		int beforeX=x;
		x-=(int)a+1;
		if (x<10)
			x=10;
		for (platform q:p){
			if (y-q.getY()+50>300)
					break;
 			if (beforeX-10>=q.getX()+q.getW()&&x-10<=q.getX()+q.getW()&&y+15>q.getY()&&y-15<q.getY()){
				jump=jumpMax;
				x=q.getX()+q.getW()+10;
			}
		}
	}
	public boolean moveUp(int grav){
			int beforey=y;
			y-=grav;
				for (platform q:p){
					if (y-q.getY()+50>300)
					break;
					if (q.getX()<x+10&&q.getX()+q.getW()>x-10&&y+10>=q.getY()&&beforey+10<=q.getY()){
						jump=jumpMax;
						y=q.getY()-10;
						return true;
					}
					if (q.getX()<x+10&&q.getX()+q.getW()>x-10&&y-10<=q.getY()+10&&beforey+10>=q.getY()+10){
						jump=jumpMax;
						y=q.getY()+21;
						return true;
					}
				}
			return false;
	}
	public void coin(){
		for (platform q:p){
			if (y-q.getY()+50>500)
					break;
			if (q.getT().equals("coin")){
				for (coin c: q.getCoin()){
					if (x+10>=c.getX()&&x-10<=c.getX()+5&&y+10>=c.getY()&&y-10<=c.getY()+5&&c.getStatus()){				
						c.pickUp();
						coins=coins+1*coinMulti;
					}
				}
			}
		}
		
	}
	public int getCoinMulti(){return coinMulti;}
	public void maxJumpIncrease(){jumpMax++;}
	public boolean getJump(){ return jump!=0;}
	public int getMaxJump(){return jumpMax;}
	public int getjump(){return jump;}
	public void jump(boolean a){
		if (a)
			jump--;
		}
	public String getJumps(){
		return ""+this.jump;	
	}
	public String getCoins(){
		return ""+this.coins;
	}
	public int igetCoins(){
		return coins;
	}
}
class panel extends JPanel implements Runnable{
	private player p;
	private gui y;
	private boolean death=true;
	private double maxTimer=10;
	private double time=0;
	private long maxTime=0;
	private double timerTracker=0;
	private int timerCount=0;
	private double timerMax=0;
	private ArrayList<Integer> maxTracker =new ArrayList<Integer>();
	private	BufferedImage img = null;
	private	BufferedImage run = null;
	private	BufferedImage run1 = null;
	private	BufferedImage run2 = null;
	private	BufferedImage run3 = null;
	private	BufferedImage run4 = null;
	private	BufferedImage run5 = null;
	private Timer timer;
	private ArrayList<laser> l;
	private boolean start=false;
	private int anime=0;
	private int maxJumpCost=125;
	private int timeCost=10;
	private int coinCost=25;
	private boolean respawn=true;
	private boolean hitbox=false;
	private boolean doAnime=false;
	private ArrayList<platform> plat=new ArrayList<platform>();
	public panel(player p,ArrayList<laser> l,gui g) 
	{

		try {
    		img  = ImageIO.read(new File("stand.png"));
    		run  = ImageIO.read(new File("run1.png"));
    		run1 = ImageIO.read(new File("run2.png"));
    		run2 = ImageIO.read(new File("run3.png"));
    		run3 = ImageIO.read(new File("run4.png"));
    		run4 = ImageIO.read(new File("run5.png"));
    		run5 = ImageIO.read(new File("run6.png"));
		} catch (IOException e) {
			anime=40;
		}
		this.l=l;
		this.y=g;
		this.p=p;
		time=maxTimer+1;
		setSize(1800, 1000);
		setVisible(true);
		p.getPlat(plat);
		plat.add(new platform(800,300,100,"normal"));
		for (int y=150; y<10000; y+=10){
			int random=(int)(Math.random()*100)+1;
			if (random>(60+(y/500))||random>95){
				plat.add(new platform((int)(Math.random()*(400+y/100))+700-y/20,0-y,(int)(Math.random()*50)+50,"normal"));
				y+=(50+y/50);
			}
			else if (random>(55+(y/200))||random>85){
				plat.add(new platform((int)(Math.random()*(700+y/100))+500-y/20,0-y,(int)(Math.random()*50)+50,"coin"));
				y+=(50+y/50);
				
			}
		}
	}
	public void startTimer(){
		start=true;
	}
	public void upgradeCoin(){
		if (coinCost==25){
			if (p.igetCoins()>=coinCost){
				p.upgradeCoin();
				p.loseCoins(coinCost);
				coinCost=50;
				checkShop();
			}
		}
		if (coinCost==50){
			if (p.igetCoins()>=coinCost){
				p.upgradeCoin();
				p.loseCoins(coinCost);
				coinCost=100;
				checkShop();
			}
		}
		if (coinCost==100){
			if (p.igetCoins()>=coinCost){
				p.upgradeCoin();
				p.loseCoins(coinCost);
				coinCost=150;
				checkShop();
			}
		}
		if (coinCost==150){
			if (p.igetCoins()>=coinCost){
				p.upgradeCoin();
				p.loseCoins(coinCost);
				coinCost=200;
				checkShop();
			}
		}
		if (coinCost==200){
			if (p.igetCoins()>=coinCost){
				p.upgradeCoin();
				p.loseCoins(coinCost);
				coinCost=275;
				checkShop();
			}
		}
		if (coinCost==275){
			if (p.igetCoins()>=coinCost){
				p.upgradeCoin();
				p.loseCoins(coinCost);
				coinCost=1000;
				checkShop();
			}
		}
	}
	public void upgradeMaxJump(){
		if (maxJumpCost==125){
			if (p.igetCoins()>=maxJumpCost){
				p.maxJumpIncrease();
				p.loseCoins(maxJumpCost);
				maxJumpCost=500;
				checkShop();
			}
			
		}
		else if (maxJumpCost==500){
			if (p.igetCoins()>=maxJumpCost){
				p.maxJumpIncrease();
				p.loseCoins(maxJumpCost);
				maxJumpCost=1000;
				checkShop();
			}	
		}

		
	}
	public void upgradeTime(){
		if (timeCost==10){
			if (p.igetCoins()>=timeCost){
				maxTimer+=5;
				p.loseCoins(timeCost);
				timeCost=25;
				checkShop();
			}
		}
		else if (timeCost==25){
			if (p.igetCoins()>=timeCost){
				maxTimer+=5;
				p.loseCoins(timeCost);
				timeCost=50;
				checkShop();
			}
		}
		else if (timeCost==50){
			if (p.igetCoins()>=timeCost){
				maxTimer+=10;
				p.loseCoins(timeCost);
				timeCost=100;
				checkShop();
			}
		}
		else if (timeCost==100){
			if (p.igetCoins()>=timeCost){
				maxTimer+=15;
				p.loseCoins(timeCost);
				timeCost=125;
				checkShop();
			}
		}
		else if (timeCost==125){
			if (p.igetCoins()>=timeCost){
				maxTimer+=20;
				p.loseCoins(timeCost);
				timeCost=175;
				checkShop();
			}
		}
		else if (timeCost==175){
			if (p.igetCoins()>=timeCost){
				maxTimer+=20;
				p.loseCoins(timeCost);
				timeCost=225;
				checkShop();
			}
		}
		else if (timeCost==225){
			if (p.igetCoins()>=timeCost){
				maxTimer+=20;
				p.loseCoins(timeCost);
				timeCost=1000;
				checkShop();
			}
		}
	}
 
 	public void respawn(){
 		death=false;
 		respawn=true;
 		time=maxTimer;
 		System.out.println("--------------");
 		maxTime=0;
 		plat=new ArrayList<platform>();
 		p.getPlat(plat);
		plat.add(new platform(850,300,100,"normal"));
		for (int y=-250; y<10000; y+=10){
			int random=(int)(Math.random()*100)+1;
			if (random>(50+(y/200))||random>95){
				plat.add(new platform((int)(Math.random()*(400+y/100))+700-y/20,0-y,(int)(Math.random()*50)+50,"normal"));
				y+=(50+y/50);
			}
			else if (random>(30+(y/500))||random>85){
				plat.add(new platform((int)(Math.random()*(700+y/100))+500-y/20,0-y,(int)(Math.random()*40)+75,"coin"));
				y+=(50+y/50);
				
			}
		}
		p.reset();
 		repaint();
		start=true;
 	}
 	public boolean getDeath(){
 		return death;
 	}
 	@Override
 	public void run(){
 		timer = new Timer();
		timer.schedule( new TimerTask() {
  	  public void run() { 
  	  	long start1 = System.nanoTime();
  	  	if (start){
  	  	if (plat.get(0).getY()-1000>p.getY())
 				plat.remove(0);
		if (respawn)
			repaint();
		if (death){
			p.reset();
			start=false;
		}
		time=time-0.025;
       if (p.getY()>1000&&!death&&respawn){
       	y.death();
    	death=true;
    	if (!death&&respawn)
    		repaint();	
    }
    long end1 = System.nanoTime(); 
    	timerTracker+=end1-start1;
    timerCount++;
   	if (end1-start1>maxTime){
    	System.out.println("Elapsed Time in nano seconds: "+ (end1-start1));  
    	maxTime=end1-start1;
    }
    if (end1-start1>timerMax/1.1){
    	timerMax=end1-start1;
    	maxTracker.add((int)end1-(int)start1);		
    }
  	  }
		}
 }, 15, 25); 
 	}
 	@Override
	public void paintComponent(Graphics g)
	{	
		long start1=System.nanoTime(); 
		if (!death){
	 		g.clearRect(0,0,this.getWidth(),this.getHeight());
	 		g.setColor(Color.DARK_GRAY);
	 		g.fillRect(0,0,this.getWidth(),this.getHeight());
	 		for (platform a:plat){
	 			g.setColor(Color.GRAY);
				if (p.getY()-a.getY()+210>500)
					break;
				if (a.getT().equals("coin")){				
					g.fillRect(a.getX(), a.getY()-p.getY()+210, a.getW(), 10);
					g.setColor(Color.YELLOW);
					ArrayList<coin> c=a.getCoin();
					for (coin s:c){
						if (s.getStatus())
							g.fillRect(s.getX(), s.getY()-p.getY()+210, 5,5);
					}
				}
				else{
					g.fillRect(a.getX(), a.getY()-p.getY()+210, a.getW(), 10);
				}
			}
			long end1=System.nanoTime(); 
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.PLAIN, 20));
			g.drawString("Jumps :"+p.getJumps(),10,20);
			g.drawString("Coins :"+p.getCoins(),10,50);
			g.drawString("Hieght :"+p.getYShow(),10,80);
			g.drawString("Timer :"+(int)time,10,110);
	 		for (int i=0; i<l.size();i++){
	 			g.fillRect(l.get(i).getX(),l.get(i).getY()-p.getY()+210,5,10);
	 			if (l.get(i).getY()-p.getY()+210<-200){
	 				l.remove(i);
	 				i--;
	 			}
	 		
	 		}
	 		g.setColor(p.getColor());
	 		if (anime==40||hitbox)
	 			g.fillRect(p.getX()-10, 200, 20, 20);
	 		else{
	 			if (anime<5&&doAnime){
	 				if (y.getRight())	
	 					g.drawImage(run,p.getX()-7,195,this);
	 				else
	 					g.drawImage(run3,p.getX()-7,195,this);
	 				anime++;
	 			}
	 			else if (anime<10&&doAnime){
	 				if (y.getRight())
	 					g.drawImage(run1,p.getX()-7,195,this);
	 				else
	 					g.drawImage(run4,p.getX()-7,195,this);
	 				anime++;
	 			}
	 			else if (anime<15&&doAnime){
	 				if (y.getRight())
	 					g.drawImage(run2,p.getX()-7,195,this);
	 				else
	 					g.drawImage(run5,p.getX()-7,195,this);
	 				anime++;
	 			}
	 			else if (anime<20&&doAnime){
	 				if (y.getRight())
	 					g.drawImage(run1,p.getX()-7,195,this);
	 				else
	 					g.drawImage(run4,p.getX()-7,195,this);
	 				anime++;
	 			}
	 			else if(doAnime){
	 				g.drawImage(run,p.getX()-7,195,this);
	 				anime=0;
	 			}
	 			else
	 				g.drawImage(img,p.getX()-7,195,this);
	 		}
		}
		if ((int)time==0)
			death=true;
	  	if (death){
	  		g.setColor(Color.RED);
	  		g.fillRect(0,0,this.getWidth(),this.getHeight());
	  		g.setColor(Color.BLACK);
	  		g.setFont(new Font("Verdana", Font.PLAIN, 20));
		  	if ((int)time==0)
		  		g.drawString("YOU RAN OUT OF TIME",this.getWidth()/2-50,this.getHeight()/2);
		  	else
		  		g.drawString("YOU DIED GET GOOD",this.getWidth()/2-50,this.getHeight()/2);
		  	g.drawString("e to go to upgrades",this.getWidth()/2-50,this.getHeight()/2+20);
		  	g.drawString("\n r to respawn",this.getWidth()/2-50,this.getHeight()/2+40);
	  	}
	  	
	}
	public void endGame(){
		timer.cancel();
		Graphics g=this.getGraphics();
		g.setColor(Color.GREEN);
		
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.setFont(new Font("Verdana", Font.PLAIN, 50));
		g.setColor(Color.BLACK);
		g.drawString("YOU WIN :):):):):):):):):):)",200,500);
	}
	public boolean checkShop(){
		if (death){
			respawn=false;
			Graphics g=this.getGraphics();
			g.clearRect(0,0,this.getWidth(),this.getHeight());
	 		g.setColor(Color.DARK_GRAY);
	 		g.fillRect(0,0,this.getWidth(),this.getHeight());
	 		g.setColor(Color.BLACK);
	 		g.setFont(new Font("Verdana", Font.PLAIN, 20));
	 		if (timeCost!=1000)
	 			g.drawString("press t to upgrade time. Cost: "+timeCost+"                                                                           Current Max time: " +maxTimer+"                                                                                                                                                                                                             Coins: "+p.getCoins(),50,50);
			else
				g.drawString("press t to upgrade time. Cost: "+"MAX"+"                                                                           Current Max time: " +maxTimer+"                                                                                                                                                                                                             Coins: "+p.getCoins(),50,50);
			if (maxJumpCost!=1000)
				g.drawString("press y to upgrade max jump. Cost: "+maxJumpCost+"                                                                           Current Max jump: " +p.getMaxJump(),50,100);
			else
				g.drawString("press y to upgrade max jump. Cost: "+"MAX"+"                                                                           Current Max jump: " +p.getMaxJump(),50,100);
			if (coinCost!=1000)
				g.drawString("press u to upgrade coin mulitplier. Cost: "+coinCost+"                                                                           Current Coin Mulitplier: " +p.getCoinMulti(),50,150);
			else
				g.drawString("press u to upgrade coin mulitplier. Cost: "+"MAX"+"                                                                           Current Coin Mulitplier: " +p.getCoinMulti(),50,150);
			if (y.getHieghtCost()!=1000)
				g.drawString("press g to upgrade jump hieght. Cost: "+ y.getHieghtCost(),50,200);
			else
				g.drawString("press g to upgrade jump hieght. Cost: "+"MAX",50,200);
			if (y.getMovCost()!=1000)
				g.drawString("press h to upgrade movment speed. Cost: "+y.getMovCost(),50,250);
			else
				g.drawString("press h to upgrade movment speed. Cost: "+"MAX",50,250);
			return true;
		}
		return false;
	}
	public void doAnime(){
		doAnime=true;
	}
	public void noAnime(){
		doAnime=false;
		anime=0;
	}
	public void hsHitbox(){
		hitbox=!hitbox;
	}
	public void debug(){
		System.out.println(p.getY());
		System.out.println("Average Time: "+ timerTracker/timerCount);
		System.out.println("Max: "+ maxTracker);
	}
}
class gui extends JFrame implements KeyListener{
	private player p;
	private ArrayList<laser> l=new ArrayList<laser>();
	private Thread thread1;
	private boolean right=false;
	private boolean down=false;
	private boolean left=false;
	private boolean up=false;
	private boolean release=true;
	private boolean shopActive=false;
	private panel m;
	private double grav=0.0;
	private double maxGrav=20.0;
	private double maxM=7;
	private double momL=0;
	private double momR=0;
	private int movCost=8;
	private int hieghtCost=8;
	public gui(){
	p=new player(Color.RED);
	}
	public void start(){
		JFrame j = new JFrame("climing stick");
		m = new panel(p,l,this);
		j.setSize(m.getSize());
		j.addKeyListener(this);	
		j.add(m); 
		j.setResizable(false);      
		j.setVisible(true); 
		m.setBackground(Color.BLACK); 
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thread1=new Thread(m);
		thread1.start();
		timer();
	}
	public void death(){
		shopActive=false;
	}
	public void timer(){
		Timer timer = new Timer();
		timer.schedule( new TimerTask() {
  	  public void run() { 
		for (laser n:l){
			n.move();
		}
		if (getRight()){
       		momL=0;
       		momR+=momR/2.5+.75;
       		if (momR>maxM)
       			momR=maxM;
       		p.moveRight(momR);
       }	
       	if(p.moveUp((int)grav))
       		grav=0;
       	grav-=((Math.abs(grav)/12.0)+.6);
       if (getLeft()){
       		momR=0;
       		momL+=momL/2.5+.75;
       		if (momL>maxM)
       			momL=maxM;
       		p.moveLeft(momL);
       }
       p.coin();
       if (p.getY()<=-10000){
       
       		thread1.stop();
       		m.endGame();
       		timer.cancel();
       }
       }
 	}, 0, 25); 
	
	}
	public panel getPanel(){
		return m;
	}
		public void momLeset(){
 		momL=0;
 	}
 	public int getHieghtCost(){
 		return hieghtCost;
 	}
 	public int getMovCost(){
 		return movCost;
 	}
 	public void momReset(){
 		momR=0;
 	}
 	public void up(boolean s){
 		if (p.getJump()&&s&&p.getMaxJump()==p.getjump()){
 			grav=maxGrav;
 			p.jump(true);
 		}
 		else if(p.getJump()&&s){
 			double temp=grav;
 			temp=maxGrav*1.2-8;
 			if (temp>grav)
 				grav=temp;
 			else
 				grav=grav+maxGrav/3;
 			p.jump(true);
 		}
 	}
 	public void down(){
 		grav=-50;
 		p.jump(false);
 	}
 	public void gravReset(){
 		grav=0;
 	}
 	public void upgradeMov(){
 		if (movCost==8){
			if (p.igetCoins()>=movCost){
				maxM++;
				p.loseCoins(movCost);
				movCost=15;
				m.checkShop();
			}
		}
		else if (movCost==15){
			if (p.igetCoins()>=movCost){
				maxM++;
				p.loseCoins(movCost);
				movCost=25;
				m.checkShop();
			}
		}
		else if (movCost==25){
			if (p.igetCoins()>=movCost){
				maxM++;
				p.loseCoins(movCost);
				movCost=50;
				m.checkShop();
			}
		}
		else if (movCost==50){
			if (p.igetCoins()>=movCost){
				maxM++;
				p.loseCoins(movCost);
				movCost=100;
				m.checkShop();
			}
		}
		else if (movCost==100){
			if (p.igetCoins()>=movCost){
				maxM++;
				p.loseCoins(movCost);
				movCost=150;
				m.checkShop();
			}
		}
		else if (movCost==150){
			if (p.igetCoins()>=movCost){
				maxM++;
				p.loseCoins(movCost);
				movCost=1000;
				m.checkShop();
			}
		}
 	}
 	public void upgradeHieght(){
 		if (hieghtCost==8){
			if (p.igetCoins()>=hieghtCost){
				maxGrav++;
				p.loseCoins(hieghtCost);
				hieghtCost=15;
				m.checkShop();
			}
		}
		else if (hieghtCost==15){
			if (p.igetCoins()>=hieghtCost){
				maxGrav++;
				p.loseCoins(hieghtCost);
				hieghtCost=25;
				m.checkShop();
			}
		}
		else if (hieghtCost==25){
			if (p.igetCoins()>=hieghtCost){
				maxGrav+=2;
				p.loseCoins(hieghtCost);
				hieghtCost=50;
				m.checkShop();
			}
		}
		else if (hieghtCost==50){
			if (p.igetCoins()>=hieghtCost){
				maxGrav+=2;
				p.loseCoins(hieghtCost);
				hieghtCost=100;
				m.checkShop();
			}
		}
		else if (hieghtCost==100){
			if (p.igetCoins()>=hieghtCost){
				maxGrav++;
				p.loseCoins(hieghtCost);
				hieghtCost=150;
				m.checkShop();
			}
		}
		else if (hieghtCost==150){
			if (p.igetCoins()>=hieghtCost){
				maxGrav+=2;
				p.loseCoins(hieghtCost);
				hieghtCost=1000;
				m.checkShop();
			}
		}
 	}
	public boolean getLeft(){return left;}
	public boolean getRight(){return right;}
	public boolean getDown(){return down;}
	public boolean getUp(){return up;}
	@Override
	public void keyTyped(KeyEvent e){}
	@Override
	public void keyPressed(KeyEvent e){

		if (e.getKeyChar()=='s'){
			down=true;
			down();
		}
		if (e.getKeyChar()=='d'){
			right=true;
			m.doAnime();
		}
		if (e.getKeyChar()=='w'){
			up=true;
			up(release);
			release=false;
			
		}
		if (e.getKeyChar()=='a'){
			left=true;
			m.doAnime();
		}
		if (e.getKeyChar()=='e'){
			shopActive=m.checkShop();

		}
	}
	@Override
	public void keyReleased(KeyEvent e){
		if (e.getKeyChar()=='s'){
			down=false;
		}
		if (e.getKeyChar()=='n'){
			l.add(new laser(p.getX(),p.getY()));
		}
		if (e.getKeyChar()=='h'&&shopActive){
			upgradeMov();
		}
		if (e.getKeyChar()=='g'&&shopActive){
			upgradeHieght();
		}
		if (e.getKeyChar()=='q'){
			m.hsHitbox();
		}
		if (e.getKeyChar()=='d'){
			right=false;
			momReset();
			m.noAnime();
		}
		if (e.getKeyChar()=='w'){
			up=false;
			release=true;
		}
		if (e.getKeyChar()=='a'){
			left=false;
			momLeset();
			m.noAnime();
		}
		if (e.getKeyChar()=='l'){
			m.respawn();
			grav=0;
		}
		if (m.getDeath()&&e.getKeyChar()=='r'){
			m.respawn();
			grav=0;
		}
		if (e.getKeyChar()=='t'&&shopActive)
			m.upgradeTime();
		if (e.getKeyChar()=='y'&&shopActive)
			m.upgradeMaxJump();
		if (e.getKeyChar()=='u'&&shopActive)
			m.upgradeCoin();
		if (e.getKeyChar()=='p')
			m.debug();
	}
}