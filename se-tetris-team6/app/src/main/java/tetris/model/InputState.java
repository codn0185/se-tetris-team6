package tetris.model;

public final class InputState {
  private boolean left, right, softDrop;
  private boolean rotateCW, rotateCCW, hardDrop, hold;

  // 지속 입력
  public void setLeft(boolean v){ left = v; }
  public void setRight(boolean v){ right = v; }
  public void setSoftDrop(boolean v){ softDrop = v; }
  public boolean isLeft(){ return left; }
  public boolean isRight(){ return right; }
  public boolean isSoftDrop(){ return softDrop; }

  // 일회성 입력(틱마다 pop)
  public void pressRotateCW(){ rotateCW = true; }
  public void pressRotateCCW(){ rotateCCW = true; }
  public void pressHardDrop(){ hardDrop = true; }
  public void pressHold(){ hold = true; }

  public boolean popRotateCW(){ boolean t=rotateCW; rotateCW=false; return t; }
  public boolean popRotateCCW(){ boolean t=rotateCCW; rotateCCW=false; return t; }
  public boolean popHardDrop(){ boolean t=hardDrop; hardDrop=false; return t; }
  public boolean popHold(){ boolean t=hold; hold=false; return t; }
}