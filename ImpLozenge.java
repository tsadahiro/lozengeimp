
import java.util.*;

public class ImpLozenge{

    int size;
    HashMap<Coord,Integer> dir;
    HashMap<Coord,Integer> impurities;
    static int typeA = 1, typeB = 2, typeC = 3;

    ImpLozenge(int s){
	size = s; // length of an edge of the triangle
	dir = new HashMap<Coord,Integer>();
	impurities = new HashMap<Coord,Integer>();

	for (int i = 0; i < size; i++){
	    for (int j = 0; j < size -i; j++){
		Coord c = new Coord(i,j);
		HashSet<Move> moveset = new HashSet<Move>();
		dir.put(new Coord(i,j),0); // initial configuration
	    }
	}
	for (int i = 0; i < size; i+=2){
	    Coord c = new Coord(i, size - i -1);
	    impurities.put(c,0);
	}
    }

    void move(Coord c, Move m){

	if (m.type == typeA){ // rotate a unit triangle
	    dir.put(c, m.angle);
	    impurities.put(c, m.angle);
	}
	else if (m.type == typeB){
	    int[] ds =     {0,1,0,2,1,2}; // current direction from white
	    int[] newdir = {1,0,2,0,2,1}; // new     direction from white
	    int[] dx =     {0,1,-1,-1,1,0};
	    int[] dy =     {-1,-1,0,1,0,1};
	    int[] ox =     {1,0,-1,-1,0,1};
	    int[] oy =     {-1,-1,1,0,1,0};

	    for (int i = 0; i < ds.length; i++){
		Coord newsite = new Coord(c.x + dx[i], c.y + dy[i]);
		Coord oldsite = new Coord(c.x + ox[i], c.y + oy[i]);
		if (dir.get(c) == ds[i] & m.angle == newdir[i]){
		    dir.put(c,m.angle);
		    impurities.remove(oldsite);
		    impurities.put(newsite,ds[i]);
		    break;
		}
	    }
	}
	else if (m.type == typeC){
	    int[] ds =     {0,2}; // current direction from white
	    int[] newdir = {2,0}; // new     direction from white
	    int[] vx =     {0,1};
	    int[] vy =     {-1,-1};
	    int[] wx =     {1,0};
	    int[] wy =     {-1,-1};

	    for (int i = 0; i < ds.length; i++){
		Coord v = new Coord(c.x + vx[i], c.y + vy[i]);
		Coord w = new Coord(c.x + wx[i], c.y + wy[i]);
		if (dir.get(c) == ds[i] & m.angle == newdir[i]){
		    dir.put(c,m.angle);
		    break;
		}
	    }

	}
    }




    HashMap<Coord,HashSet<Move>>  getMovable(){
	HashMap<Coord,HashSet<Move>> possiblemoves = new HashMap<Coord,HashSet<Move>>();
	for (int i = 0; i < size; i++){
	    for (int j = 0; j < size -i; j++){
		Coord c = new Coord(i,j);
		HashSet<Move> moveset = new HashSet<Move>();
		possiblemoves.put(c, moveset); // insert the empty set
	    }
	}
	for (Map.Entry<Coord,Integer> e: dir.entrySet()){
	    getTypeAaround(e.getKey(),e.getValue(), possiblemoves);
	    getTypeBaround(e.getKey(),e.getValue(), possiblemoves);
	}
	return possiblemoves;
    }

    void getTypeAaround(Coord c, int direction,HashMap<Coord,HashSet<Move>> possiblemoves){
	if (impurities.get(c) != null && impurities.get(c) == direction){
	    possiblemoves.get(c).add(new Move(typeA, (direction+1)%3));
	    possiblemoves.get(c).add(new Move(typeA, (direction+2)%3));
	}
    }

    void getTypeBaround(Coord c, int direction, HashMap<Coord,HashSet<Move>> possiblemoves){
	int[] ds = {0,0,1,1,2,2};
	int[] dx = {-1,1,0,0,1,-1};
	int[] dy = {1,-1,-1,1,0,0};
	int[] impdir = {2,1,0,2,1,0};
	int[] newdir = {2,1,0,2,1,0};

	for (int i = 0; i < dx.length; i++){
	    Coord f = new Coord(c.x + dx[i], c.y + dy[i]);
	    if (direction == ds[i] && impurities.get(f) != null && impurities.get(f) == impdir[i]){
		possiblemoves.get(c).add(new Move(typeB, newdir[i]));
		return;
	    }
	}
    }

    public boolean isValid(){
	
	for (int i = 0; i < size -1; i++){
	    for (int j = 0; j < size -1; j++){
		Coord c = new Coord(i,j);
		Coord d = new Coord(i+1,j);
		Coord e = new Coord(i,j+1);
		if (dir.get(c) == null || dir.get(d) == null || dir.get(e) == null){
		    continue;
		}
		//System.out.println(dir.get(c) + " " + dir.get(e));
		
		if (dir.get(c) == 1 && dir.get(d) ==0){
		    return(false);
		}
		if (dir.get(c) == 2 && dir.get(e) ==0){
		    return(false);
		}
	    }
	}
	return(true);
    }

    @Override
    public String toString(){
	String str = "";
	Coord c;
	for (int j = 0; j < size; j++){
	    for (int i = 0; i < size; i++){
		c = new Coord(i,j);
		if (dir.get(c)==null){
		    continue;
		}
		str += dir.get(c);
	    }
	}
	return str;
    }

    @Override
    public int hashCode(){
	int hc = 0;
	for (Map.Entry<Coord,Integer> e: dir.entrySet()){
	    hc = hc*3 + e.getValue();
	}
	return hc;
    }

    @Override
    public boolean equals(Object obj){
	return(this.hashCode()==((ImpLozenge)obj).hashCode());
    }

    public static void main(String[] args){
	ImpLozenge tiling = new ImpLozenge(3);
    }

}

class Coord{
    int x;
    int y;

    Coord(int x, int y){
	this.x = x;
	this.y = y;
    }

    @Override
    public String toString(){
	return(x + "," + y);
    }

    @Override
    public boolean equals(Object obj){
	return(this.hashCode()==((Coord)obj).hashCode());
    }
    
    @Override
    public int hashCode(){
	return 10000*x + y;
    }

	    
}

class Move{

    int type;
    int angle; // 0 west, 1 south-east, 2 north-east

    Move(int t, int a){
	this.type = t;
	this.angle = a;
    }

    @Override
    public String toString(){
	if (type == ImpLozenge.typeA){
	    return("A: " + angle);
	}
	else if (type == ImpLozenge.typeB){
	    return("B: " + angle);
	}
	else{
	    return("C: " + angle);
	}
    }

    @Override
    public boolean equals(Object obj){
	return(this.hashCode()==((Move)obj).hashCode());
    }
    
    @Override
    public int hashCode(){
	return 10*type + angle;
    }

}
