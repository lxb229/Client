export default class MJCard {
    point: number;//点数1-9
    suit: number;//花色1-3,1是万，2是筒，3是条
    constructor(point: number, suit: number) {
        this.point = point;
        this.suit = suit;
    }
}