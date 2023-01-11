import { GameObject } from "./GameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends GameObject {
    constructor(ctx, parent, store){
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;
        this.rows = 13;
        this.cols = 14;
        this.walls = [];
        this.inner_walls_count = 20;
        this.snakes = [
            new Snake({id: 1, color: '#4876EC', r: this.rows - 2, c: 1}, this),
            new Snake({id: 2, color: '#F94848', r: 1, c: this.cols - 2}, this)
        ]
    }


    create_walls(){
        const g = this.store.state.pk.gamemap;
        for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if(g[r][c]){
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    add_listening_events(){
        if(this.store.state.record.is_record) {
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            const [snake0, snake1] = this.snakes;
            let k = 0;
            const interval_move = setInterval(function() {
                if(k < a_steps.length - 1) {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }else {
                    if(loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }else if(loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_move);
                }
                k++;
            }, 300);
        }else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                let direction = -1;
                if (e.key === 'w') direction = 0;
                else if (e.key === 'd') direction = 1;
                else if (e.key === 's') direction = 2;
                else if (e.key === 'a') direction = 3;
                
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: direction
                }));
            });
        }
    }

    start(){
        this.create_walls();
        this.add_listening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_snake_ready(){
        for(const snake of this.snakes){
            if(snake.status !== 'idle'){
                return false;
            }
            if(snake.direction === -1){
                return false;
            }
        }
        return true;
    }

    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_next_step_vaild(cell){
        for(let wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c){
                return false;
            }
        }
        for(let snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){
                k --;
            }
            for(let i = 0; i < k; i ++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c){
                    return false;
                }
            }
        }
        return true;
    }

    update(){
        this.update_size();
        if(this.check_snake_ready()){
            this.next_step();
        }
        this.render();
    }

    render(){
        this.ctx.fillStyle = 'green';
        this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
        const color_even = "#AAD751", color_odd = "#A2D149";
        for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if((r + c) % 2 == 0){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}