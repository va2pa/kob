import { Cell } from "./Cell";
import { GameObject } from "./GameObject";

export class Snake extends GameObject{
    constructor(info, gamemap){
        super();
        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.cells = [new Cell(info.r, info.c)];
        this.speed = 5;
        this.direction = -1;
        this.status = "idle";
        this.next_cell = null;

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];
        this.step = 0;
        this.eps = 1e-2;
        
    }
    start(){

    }

    set_direction(d){
        this.direction = d;
    }

    next_step(){
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        for(let i = k; i > 0; i --){
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    check_tail_increasing(){
        if(this.step < 5){
            return true;
        }
        return this.step % 3;
    }

    update_move(){
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        if(distance < this.eps){
            // this.cells[0].r = this.next_cell.r;
            // this.cells[0].c = this.next_cell.c;
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            this.status = "idle";
            if(!this.check_tail_increasing()){
                this.cells.pop();
            }
        }else{
            const move_distance = this.speed * this.timedelta / 1000;
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
            if(!this.check_tail_increasing()){
                const k = this.cells.length;
                const tail = this.cells[k - 1];
                const target_tail = this.cells[k - 2];
                const tail_dx = target_tail.x - tail.x;
                const tail_dy = target_tail.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
        
    }

    update(){
        if(this.status === 'move'){
            this.update_move();
        }
        this.render();
    }

    render(){
        const ctx = this.gamemap.ctx;
        const L = this.gamemap.L;
        ctx.fillStyle = this.color;
        for(let cell of this.cells){
            ctx.beginPath()
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, 2 * Math.PI);
            ctx.fill();
        }

        for(let i = this.cells.length - 1; i > 0; i --){
            let a = this.cells[i];
            let b = this.cells[i - 1];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps){
                continue;
            }else if(Math.abs(a.x - b.x) < this.eps){    // 竖直方向
                ctx.fillRect((a.x - 0.5 * 0.8) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            }else{      // 水平方向
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.5 * 0.8) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }
    }
}