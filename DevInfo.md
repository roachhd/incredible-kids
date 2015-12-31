# 드래그&드랍, 줄 긋기 구현 방안(Andengine) #

선, 박스 등을 하나의 entity로 두고 배경위에 올린뒤에 모양 및 위치를 바꾸는 형식으로 구현 가능함. 자세한 사항은 TestProject 소스를 svn을 이용해 받아 볼것

  * Andengine을 이용한 드래그&드랍 구현

```
final Sprite word = new Sprite(centerX, centerY, this.mFaceTextureRegion) {
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP && m_bCollide){
			this.setPosition(mbox.getX(), mbox.getY());
		}else{
			this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		}
		return true;
	}
			
};	
```

  * Andengine을 이용한 줄긋기 구현

```
line = new Line(0,0,0,0,5f);
line.setColor(0.0f,0.0f,0.0f);

mbox = new Sprite(centerX, centerY, this.mBoxTextureRegion) {
			
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		line.setPosition(mbox.getX(), mbox.getY(), pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
	}
};	
```