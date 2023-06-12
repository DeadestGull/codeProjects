import java.awt.*;  
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.util.TimerTask;
import java.util.Timer;
public class bullet_swarm{
	public static void main(String args[])
	{
		frame f=new frame();
		f.reset();
	}
}
class background{
	public void draw(Graphics g,int c,Color color){	
	}
}
class astroid extends background{
	private double x;
	private double y;
	private double angle;
	private int size;
	int []xList;
	int []yList;
	public astroid(int x, int y, double a,int s){
		this.x=x;
		this.y=y;
		angle=a;
		size=s;
		xList=new int[]{x-size*3,x-size*6,x-size*2-20,x-size*2-3,x,x+size*2,x+size*2};
		yList=new int[]{y-size*6,y-size*3,y+size+3,y+size*6,y+size*7,y+size*6,y};
		}
	public void draw(Graphics g,int c,Color color){
		g.fillPolygon(xList,yList,7);
		move();
	}
	public void move(){
    	y += 1;
    	int y=(int)this.y;
		yList=new int[]{y-size*6,y-size*3,y+size+3,y+size*6,y+size*7,y+size*6,y};
	}
}
class star extends background{
	private double x;
	private double y;
	private int size;
	public star(int x, int y, int s){
		this.x=x;
		this.y=y;
		size=s;
	}
	public void draw(Graphics g,int c,Color color){
		if (c==-1)
			g.setColor(new Color(230,230,230));
		else
			System.out.println(c);
		g.fillRect((int)x,(int)y,size*2,size*2);
		move();
		g.setColor(color);
	}
	public void move(){
		y+=1;
	}
}
class powerUp{
	private double x;
	private double y;
	private double angle;
	public powerUp(int x, int y, double angle){
		this.x=x;
		this.y=y;
		this.angle=angle;
	}
	public void move(){
		 x += (angle-90)/90.0*(4.0);
    	 y += (Math.abs(angle-90)-90)/-90.0*(4.0);
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public void paint(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillOval((int)x,(int)y,30,30);
	}
	public int isTouchingPlayer(int px, int py){
		double distanceBetween=Math.sqrt((-y+py-15)*(-y+py-15)+(x-px+15)*(x-px+15));
		if (distanceBetween<=40){
			return 1;
		}
		return -1;
		
	}
}
class life extends powerUp{
	private int width=30;
	private int height=30;
	public life(int x, int y, double angle){
		super(x,y,angle);
	}
	public void paint(Graphics g){
		g.setColor(Color.RED);
		int[] triangleX = {getX()-2*width/18,getX()+width+2*width/18,(getX()-2*width/18+getX()+width+2*width/18)/2};
    	int[] triangleY = {getY()+height-2*height/3, getY()+height-2*height/3, getY()+height};
    	g.fillOval(getX() - width/12,getY(), width/2 + width/6, height/2); 
   		g.fillOval(getX() + width/2 - width/12,getY(),width/2 + width/6,height/2);
    	g.fillPolygon(triangleX, triangleY, triangleX.length);
    	
	}
	public int isTouchingPlayer(int px, int py){
		if (super.isTouchingPlayer(px,py)==1)
			return 0;
		return -1;
	}
}
class immune extends powerUp{
	public immune(int x, int y, double angle){
		super(x,y,angle);
	}
	public void paint(Graphics g){
		g.setColor(Color.BLUE);
		g.fillRect(getX()+7,getY()-7,20,40);
	}
	public int isTouchingPlayer(int px, int py){
		if (super.isTouchingPlayer(px,py)==1)
			return 1;
		return -1;
	}
}
class enemy{
	private boolean leave;
	private int attackCooldown;
	private int burst;
	private int currentBurst=1;
	private int x;
	private int y;
	private double attackSpeed;
	private double incrament;
	private game g;
	private int angleChange;
	private int speed;
	private boolean attackRight=true;
	private int angle;
	private int size;
	private int lockY;
	public enemy(int b,int as,double i,int x,int y,game g,int angle,int speed,int size,int ac,boolean l){
		this.x=x;
		leave=l;
		this.speed=speed;
		this.g=g;
		this.y=0;
		lockY=y;
		angleChange=ac;
		this.size=size;
		burst=b;
		attackSpeed=as;
		incrament=i;
		this.angle=angle;
	}
	public boolean checkLeave(){
		
		if (leave){
			attackSpeed=0;
			attackCooldown=0;
			y-=10;
		}
		if (y<0)
			return true;
		return false;
	}
	public void leave(){
		leave=true;
	}
	public boolean move(){
		if (y!=lockY){
			y+=5;
			if (y>lockY)
				y=lockY;
		}
		else if (attackCooldown>=200){
			if (currentBurst<burst)
				currentBurst++;
			else{
				attackCooldown=0;
				currentBurst=1;
				attackSpeed+=incrament;
			}
			return true;
		}
		else
			attackCooldown+=(int)attackSpeed;
		return false;
	}
	public void changeInc(double a){
		incrament=a;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}

	public void attack(int px, int py){
		g.addBullet(x,y,180-Math.toDegrees(Math.atan2((double)py-y,((double)px-x)))-(Math.random()*burst*2+1-burst),speed,size);
	}
	public void attack(){
		g.addBullet(x,y,angle,speed,size);
		if (angle>=180){
			attackRight=false;
		}
		if (angle<=0){
			attackRight=true;	
		}
		if (attackRight){
			angle+=angleChange;		
		}
		else{
			angle-=angleChange;		
		}
	}
}
class seeker extends enemy{
	public seeker(game g,int x, int y, int speed, int burst,int as,int size,double i,boolean leave){
		super(burst,as,i,x,y,g,-100,speed,size,0,leave);
	}

}
class basic extends enemy{
	public basic(game g,int x,int y,int s,int b,int as,int size,int a,double i, int ac,boolean leave){
		super(b,as,i,x,y,g,a,s,size,ac,leave);

	}
}
class bullet{
	private double x;
	private double y;
	private double xchange, ychange;
	private int size;
	public bullet(int x, int y,double a,int s,int size){
		this.x=x;
		this.y=y;	
		this.size=size;
		xchange = (a-90)/90.0*(s/4.0);
		ychange= (Math.abs(a-90)-90)/-90.0*(s/4.0);
	}
	public void move(){
		 x += xchange;
    	 y += ychange;
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public int getSize(){
		return size;
	}
	public boolean isTouchingPlayer(int px, int py, int psize){
		double distanceBetween=Math.sqrt((-y+py)*(-y+py)+(x-px)*(x-px));
		if (distanceBetween<=psize/2){
			return true;
		}
		return false;
	}
}
class player implements MouseListener,KeyListener{
	private int x=500;
	private int y=600;
	private int life=5;
	private boolean up=false;
	private boolean down=false;
	private boolean left=false;
	private boolean right=false;
	private boolean boost;
	private boolean resetBoost;
	private double boostCharge=100;
	public player(){
		
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public double getBoost(){
		return boostCharge;
	}
	public void move(){
		int moveSpeed=10;
		if(resetBoost&&boostCharge<=100){
			boostCharge+=.05;
		}
		else if (boost){
			boostCharge-=.2;
			moveSpeed=20;
			if (boostCharge<=0)
				resetBoost=true;
		}
		else if(boostCharge<=100)
			boostCharge+=.05;
		if (left)
			x-=moveSpeed;
		if (right)
			x+=moveSpeed;
		if (up)
			y-=moveSpeed;
		if (down)
			y+=moveSpeed;
		if (x<25)
			x=25;
		if (x>1760)
			x=1760;
		if (y>935)
			y=935;
		if (y<125)
			y=125;
	}
	public void loseLife(int a){
		life-=a;
	}
	public int getLife(){
		return life;
	}
	public void sheildHit(){
		boostCharge-=15;
	}
	public boolean hasSheild(){
		return boost&&!resetBoost&&boostCharge>=25;
	}
	@Override 
	public void mousePressed(MouseEvent e){
		if (e.getButton()==e.BUTTON1)
			boost=true;
			resetBoost=false;
	}
	@Override 
	public void mouseClicked(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseReleased(MouseEvent e){
		if (e.getButton()==e.BUTTON1)
			boost=false;
	}
	@Override
	public void keyTyped(KeyEvent e){}
	@Override
	public void keyPressed(KeyEvent e){
		if (e.VK_LEFT==e.getKeyCode()||e.VK_A==e.getKeyCode()){
			left=true;
		}
		if (e.VK_RIGHT==e.getKeyCode()||e.VK_D==e.getKeyCode()){
			right=true;
		}
		if (e.VK_UP==e.getKeyCode()||e.VK_W==e.getKeyCode()){
			up=true;
		}
		if (e.VK_DOWN==e.getKeyCode()||e.VK_S==e.getKeyCode()){
			down=true;
		}
	}
	@Override
	public void keyReleased(KeyEvent e){
		if (e.VK_LEFT==e.getKeyCode()||e.VK_A==e.getKeyCode()){
			left=false;
		}
		if (e.VK_RIGHT==e.getKeyCode()||e.VK_D==e.getKeyCode()){
			right=false;
		}
		if (e.VK_UP==e.getKeyCode()||e.VK_W==e.getKeyCode()){
			up=false;
		}
		if (e.VK_DOWN==e.getKeyCode()||e.VK_S==e.getKeyCode()){
			down=false;
		}
	}
}
class frame extends JFrame {
	private double total=0;
	private int times=0;
	private boolean startMenu=false;
	private player p;
	private JFrame j;
	private panel pan;
	private ArrayList<button> buttons;
	public frame(){
		j = new JFrame("bullet swarm");
		p=new player();
		pan=new panel(p);
		buttons=new ArrayList<button>();
		pan.getButtons(buttons);
		buttons.add(new button(600,300,500,200,"Start"));
		j.setSize(pan.getSize());	
		j.add(pan); 
		j.addKeyListener(p);
		j.addMouseListener(p);
		j.addMouseListener(buttons.get(0));
		j.setResizable(false);      
		j.setVisible(true); 
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void start(){
		Timer timer = new Timer();
		timer.schedule( new TimerTask() {
  	  		public void run() {
  	  			if (startMenu)	
  	  			{
  	  				Long start=System.currentTimeMillis();
  	  				p.move();
  	  				pan.background();
					pan.phase();
					pan.move();
					pan.colorChange();
					pan.spawnPow();
					Long end=System.currentTimeMillis();
					total+=(end-start);
					times++;
					if (end-start>1){
						System.out.println(end-start+" ");
						System.out.println("average:"+total/times);
					}
  	  			}
  	  			else
  	  			{
  	  				pan.startMenu(pan.getGraphics());
  	  				startMenu=pan.checkClick();
  	  			}
  	  			if (p.getLife()==0){
  	  				timer.cancel();
  	  				timer.purge();
  	  				reset();
  	  			}
  	  		}
		},0,30);
	}
	public void reset()
	{
		startMenu=false;
		j.remove(pan);
		p=new player();
		pan=new panel(p);
		buttons=new ArrayList<button>();
		pan.getButtons(buttons);
		buttons.add(new button(600,300,500,200,"Start"));
		j.setSize(pan.getSize());	
		j.add(pan); 
		j.addKeyListener(p);
		j.addMouseListener(p);
		j.addMouseListener(buttons.get(0));
		j.setResizable(false);      
		j.setVisible(true); 
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pan.startMenu(pan.getGraphics());
		while(!pan.checkClick());
		start();
	}
}
class game{
	private ArrayList<background> backgrounds;
	private int score=0;
	private ArrayList<basic> basics;
	private ArrayList<seeker> seekers;
	private ArrayList<bullet> bullets;
	private ArrayList<powerUp> pow;
	public game(){
		backgrounds=new ArrayList<background>();
		pow=new ArrayList<powerUp>();
		bullets=new ArrayList<bullet>();
		basics=new ArrayList<basic>();
		seekers=new ArrayList<seeker>();
		basics.add(new basic(this,100,100,20,1,20,10,0,.2,10,false));
		basics.add(new basic(this,600,100,30,1,20,10,180,.45,4,false));
		basics.add(new basic(this,1100,100,30,1,20,10,0,.45,4,false));
		basics.add(new basic(this,1600,100,20,1,20,10,180,.2,10,false));
	}
	public void addBullet(int x,int y, double angle, int speed, int size){
		bullets.add(new bullet(x,y,angle,speed,size));
	}
	public void spawnHeart(){
		int x=(int)(Math.random()*1700)+50;
		int rand=(int)(Math.random()*89)+1;
		if (x<775)
			rand+=90;
		pow.add(new life(x,100,rand));
	}
	public void spawnInv(){
		int x=(int)(Math.random()*1700)+50;
		int rand=(int)(Math.random()*89)+1;
		if (x<775)
			rand+=90;
		pow.add(new immune(x,100,rand));
	}
	public void move(){
		for (int i=0; i<pow.size();i++){
			pow.get(i).move();
			if (pow.get(i).getX()<30||pow.get(i).getX()>1755||pow.get(i).getY()>1770){
				pow.remove(i);
				i--;
			}
		}
		for (int i=0; i<bullets.size();i++){
			bullets.get(i).move();
			if (bullets.get(i).getX()<bullets.get(i).getSize()){
				bullets.remove(i);  
				i--;
				score+=10;
			}
			else if (bullets.get(i).getX()>1785-bullets.get(i).getSize()){
				bullets.remove(i);
				i--;
				score+=10;
			}
			else if (bullets.get(i).getY()>1800-bullets.get(i).getSize()){
				bullets.remove(i);
				i--;
				score+=10;
			}
		}
		
	}
	public boolean leave(){
		boolean leave=false;
		for (int i=0; i<basics.size(); i++){
			if (basics.get(i).checkLeave()){
				basics.remove(i);
				i--;
				leave=true;
			}
		}
		for (int i=0; i<seekers.size();i++){
			if (seekers.get(i).checkLeave()){
				seekers.remove(i);
				i--;
				leave=true;
			}
		}
		return leave;
	}
	public ArrayList<basic> getBasics(){
		return basics;
	}
	public ArrayList<seeker> getSeekers(){
		return seekers;
	}
	public ArrayList<powerUp> getPow(){
		return pow;
	}
	public void score(int s){
		score+=s;
	}
	public int getScore(){
		return score;
	}
	public void removeBasic(int a){
		basics.remove(a);
	}
	public void removeSeeker(int a){
		seekers.remove(a);
	}
	public void enterSeeker(int x, int y, int s, int b, int as, int size,double i){
		seekers.add(new seeker(this,x,y,s,b,as,size,i,false));
	}
	public void enterBasic(int x, int y, int s, int b, int as, int size,int a,double i,int ac){
		basics.add(new basic(this,x,y,s,b,as,size,a,i,ac,false));
	}
	public ArrayList<bullet> getBullets(){
		return bullets;
	}
	public ArrayList<background> getBackgrounds(){
		return backgrounds;
	}
	public void createAstroid(){
		if ((int)(Math.random()*100)==0){
			backgrounds.add(new astroid((int)(Math.random()*1700)+50,100,90,(int)(Math.random()*7)+3));
		}
	}
	public void createStar(){
		if ((int)(Math.random()*20)==0){
			backgrounds.add(new star((int)(Math.random()*1700)+50,100,(int)(Math.random()*5)+1));
		}
	}	
}

class panel extends JPanel{
	private int r;
	private int b;
	private int g;
	public double timeNow;
	private game game;
	private player p;
	private int phase;
	private int colorPhase;
	private Color c;
	private int sheildTime;
	private boolean start;
	private ArrayList<button> buttons;
	public panel(player p){
		r=130;
		sheildTime=0;
		b=130;
		g=130;
		timeNow=0;
		this.p=p;
		phase=0;
		colorPhase=-1;
		start=false;
		c=new Color(r,g,b);
		setSize(1800, 1000);
		setVisible(true);
		game=new game();
	}
	public void move(){
		for (basic b:game.getBasics()){
			if(b.move())
				b.attack();
		}
		for (seeker s:game.getSeekers()){
			if(s.move())
				s.attack(p.getX(),p.getY());
		}
		game.move();
	}
	public void phase(){
		int change=phase;
		if ((phase==2||phase==0||phase==4)&&timeNow>1500){
			phase++;
			for (basic b:game.getBasics()){
				b.leave();
			}
			for (seeker s:game.getSeekers()){
				s.leave();
			}
			timeNow=0;
		}
		else if (phase==1&&timeNow>100){
			timeNow=0;
			game.enterSeeker(150,100,40,1,15,10,.1);
			game.enterSeeker(550,100,40,1,15,10,.1);
			game.enterBasic(800,100,30,2,10,10,0,.35,8);
			game.enterBasic(800,100,30,2,10,10,180,.35,8);
			game.enterSeeker(1250,100,40,1,15,10,.1);
			game.enterSeeker(1650,100,40,1,15,10,.1);			
			phase++;
		}		
		else if (phase==3&&timeNow>100){
			phase++;
			game.enterBasic(200,100,40,30,2,10,0,.1,6);
			game.enterBasic(600,100,40,30,2,10,0,.1,6);
			game.enterBasic(925,100,20,1,30,10,0,.25,3);
			game.enterBasic(1250,100,40,30,2,10,0,.1,6);
			game.enterBasic(1650,100,40,30,2,10,0,.1,6);
		}
		else if (phase==5&&timeNow==100){
			game.enterSeeker(150,100,40,3,20,10,.1);
			game.enterSeeker(1650,100,40,3,20,10,.1);
		}
		else if (phase==5&&timeNow==100){
			game.enterSeeker(450,100,40,5,15,10,.1);
			game.enterSeeker(1350,100,40,5,15,10,.1);
		}
		else if (phase==5&&timeNow==100){
			colorPhase++;
			phase++;
			game.enterSeeker(750,100,40,4,10,10,.1);
			game.enterSeeker(1050,100,40,4,10,10,.1);
		}
		if (timeNow<=100)
			game.leave();
		timeNow++;
	}
	public void spawnPow(){
		int rand=(int)(Math.random()*2000);
		if (rand==0)
			game.spawnHeart();
		if (rand==1)
			game.spawnInv();
	}
	public void colorChange(){
			if (colorPhase==0){
				if (r<200){
					r++;
					g++;
					b++;
				}
				else
					colorPhase++;
			}
			if(colorPhase==1){
				r+=1;
				if (r>=255){
					colorPhase=2;
					r=255;
				}
			}
			 if(colorPhase==2){
				r-=1;
				g+=1;
				if (g>=255){
					colorPhase=3;
					g=255;
				}
			}
			if(colorPhase==3){
				g-=1;
				b+=1;
				if (b>=255){
					colorPhase=4;
					b=255;
				}
			}
			if(colorPhase==4){
				b-=1;
				r+=1;
				if (r>=255){						
					colorPhase=2;
					r=255;
				}
			}
			if (colorPhase==5){
				if (r>130)
					r--;
				if (b>130)
					b--;
				if (g>130)
					g--;
				if (r<130&&b<130&&g<130)
					colorPhase=-1;
			}		
			c=new Color(r,g,b);
			repaint();
	}
	public void getButtons(ArrayList<button> buttons){
		this.buttons=buttons;
	}
	public void startMenu(Graphics g){
		for (button b:buttons){
			b.draw(g);
		}
	}
	public boolean checkClick(){
		for (button b:buttons){
			if(b.isClicked()){
				start=true;
				return true;
			}
		}
		return false;
	}
	public void background(){
		game.createAstroid();
		game.createStar();
	}
	public void paintComponent(Graphics g){
		if (!start)
		{
			startMenu(g);
		}
		else{
		background();
		g.setColor(c);
		g.fillRect(0,100,getWidth(),getHeight());
		g.setColor(c);
		g.fillRect(0,0,getWidth(),100);
		g.setColor(new Color(r-10,this.g-10,b-10));
		for (background b:game.getBackgrounds()){
			b.draw(g,colorPhase,new Color(r-10,this.g-10,this.b-10));
		}
		if (p.hasSheild()&&sheildTime>0){
			g.setColor(new Color(138,186,211));
			g.fillOval(p.getX()-40,p.getY()-40,80,80);
			g.setColor(c);
			g.fillOval(p.getX()-35,p.getY()-35,70,70);	
		}
		if (p.hasSheild()||sheildTime>0){
			g.setColor(new Color(138,186,211));	
			g.fillOval(p.getX()-30,p.getY()-30,60,60);
		}
		if (p.getLife()>3)	
			g.setColor(Color.GREEN);
		else if (p.getLife()>1)
			g.setColor(Color.YELLOW);
		else
			g.setColor(new Color(255,100,0));
		g.fillOval(p.getX()-25,p.getY()-25,50,50);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Verdana", Font.PLAIN, 20));
		g.drawString ("SHIELD",getWidth()-210,getHeight()-40);
		g.drawString ("SCORE: "+game.getScore(),10,30);
		if (sheildTime==0)
			g.drawString (""+p.getLife(),p.getX()-7,p.getY()+8);
		else
			g.drawString(""+(sheildTime/33),p.getX()-7,p.getY()+8);
		g.fillRect(getWidth()-214,getHeight()-34,208,28);
		g.setColor(c);
		g.fillRect(getWidth()-210,getHeight()-30,200,20);
		g.setColor(Color.BLACK);
		g.drawString("SHIELD ZONE",getWidth()-155,getHeight()-12);
		g.setColor(Color.BLUE);
		g.fillRect(getWidth()-210,getHeight()-30,(int)p.getBoost()*2,20);
		g.setColor(Color.BLACK);
		g.fillRect(getWidth()-160,getHeight()-30,5,20);
		int lossLife=0;
		g.setColor(Color.RED);
		for (int i=0; i<game.getBullets().size();i++){
			if(p.getLife()!=0&&game.getBullets().get(i).isTouchingPlayer(p.getX(),p.getY(),50)){
					if (p.hasSheild()){
						p.sheildHit();
					}
					else
						lossLife=1;
					game.getBullets().remove(i);
					i--;
				}
				else{
					g.fillOval(game.getBullets().get(i).getX()-game.getBullets().get(i).getSize()/2,game.getBullets().get(i).getY()-game.getBullets().get(i).getSize()/2,game.getBullets().get(i).getSize(),game.getBullets().get(i).getSize());			
				}
		}
		g.setColor(Color.BLUE);
		for (basic b:game.getBasics()){
			g.fillOval(b.getX()-10,b.getY()-10,20,20);
		}
		for (seeker b:game.getSeekers()){
			g.fillOval(b.getX()-10,b.getY()-10,20,20);
		}
		for (int i=0; i<game.getPow().size(); i++){
			game.getPow().get(i).paint(g);
			if (game.getPow().get(i).isTouchingPlayer(p.getX(),p.getY())==0){
				
				p.loseLife(-1);
				game.getPow().remove(i);
				i--;
			}
			else if (game.getPow().get(i).isTouchingPlayer(p.getX(),p.getY())==1){
				
				sheildTime=330;
				game.getPow().remove(i);
				i--;
			}
		}
		if (sheildTime>0){
			sheildTime--;
			if (lossLife==1){
				lossLife=0;
				sheildTime=0;
			}
		}
		
		p.loseLife(lossLife);
		}
	}

}
class button implements MouseListener{
	private int x;
	private int y;
	private int sizeX;
	private int sizeY;
	private String text;
	private boolean clicked=false;
	public button(int x,int y,int sx,int sy,String t){
		this.x=x;
		this.y=y;
		sizeX=sx;
		sizeY=sy;
		text=t;
	}
	public void draw(Graphics g){
		g.setColor(new Color(170,170,170));
		g.fillRect(x,y,sizeX,sizeY);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Verdana", Font.PLAIN, 50));
		g.drawString(text,(x*2+sizeX-125)/2,(y*2+sizeY+25)/2);
	}
	public boolean isClicked(){
		return clicked;
	}
	@Override 
	public void mouseClicked(MouseEvent e){
		clicked=e.getX()>x&&e.getX()<x+sizeX&&e.getY()>y&&e.getY()<y+sizeY;
	}
	@Override
	public void mouseExited(MouseEvent e){}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseReleased(MouseEvent e){}
	@Override
	public void mousePressed(MouseEvent e){}
}
