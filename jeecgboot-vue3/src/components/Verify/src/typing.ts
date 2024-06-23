export interface DragVerifyActionType {
  resume: () => void;
}

export interface PassingData {
  isPassing: boolean;
  time: number;
}

export interface MoveData {
  event: MouseEvent | TouchEvent;
  moveDistance: number;
  moveX: number;
}
