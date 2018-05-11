
function test2(param){
	this.index="index_"+param;
}

function test1(){
	this.name = "zhang";
	this.arrtest=new Array();
	for(i=0; i<5; i++){
		this.arrtest[i] = new test2(i);
	}
}
function test(){
return "person";
}