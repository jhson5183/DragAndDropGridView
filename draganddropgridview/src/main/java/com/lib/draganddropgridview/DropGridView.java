package com.lib.draganddropgridview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.List;

/**
 * 드래그앤 드랍 그리드 뷰
 * @author jhson
 *
 */
public class DropGridView extends GridView implements OnItemLongClickListener, OnDragListener{//, OnScrollListener{
	
	private DropModel dropModel = null;	//선택된 뷰의 모델
	private int targetX = 0;
	private int targetY = 0;
//	private Context context = null;
	private final int ANIMATION_DURATION = 200;
	public DragScrollControl sc = null;	//스크롤 통제

	private boolean isDrop = false;	//드롭 상태인지 파악
	public boolean isScrollAction = false;	//스크롤 통제 시작 변수

	public DropGridView(Context context){
		this(context,null);
	}

	public DropGridView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
		setOnItemLongClickListener(this);
//		this.context = context;
		sc = new DragScrollControl(this);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		//스크롤 할 때 보이지 않는 뷰를 미리 생성하기
		ViewGroup.LayoutParams params = getLayoutParams();
		if(params != null){
			params.width = ViewGroup.LayoutParams.MATCH_PARENT;
			params.height = ViewGroup.LayoutParams.MATCH_PARENT;
			setLayoutParams(params);
		}

		if(!isScrollAction){
			for (int i = 0; i < getChildCount(); i++) {
				getChildAt(i).setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	//    AdapterView.OnItemLongClickListener dragAndDropListener = new AdapterView.OnItemLongClickListener() {
//
//        @Override
//        public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
//
//            mDropGridView.isScrollAction = true;
//
//            final int lastVisibleIndex = parent.getLastVisiblePosition();
////			final int lastVisibleViewHeight = parent.getChildAt(parent.getChildCount() -1).getHeight();
//            final int viewY = (int) view.getY();
//
//            ViewGroup.LayoutParams params = parent.getLayoutParams();
//            if(params != null){
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                params.height = parent.getHeight() + view.getHeight();
//                parent.setLayoutParams(params);
//            }
//
//            parent.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    final View targetView = (View) parent.getChildAt(position - parent.getFirstVisiblePosition());
//                    targetView.startDrag(null, new View.DragShadowBuilder(targetView), targetView, 0);
//
//                    int count = parent.getChildCount();
//
//                    for (int i = 0; i < count; i++) {
//
//                        View curr = parent.getChildAt(i);
//
//                        curr.setOnDragListener((View.OnDragListener) parent);
//
//                    }
//
//                    int afterViewY = (int) view.getY();
//                    afterViewY -= viewY;
//
//                    ViewGroup.LayoutParams params = parent.getLayoutParams();
//                    if(params != null){
//                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                        parent.setLayoutParams(params);
//                    }
//
//
//                    if(lastVisibleIndex == mList.size() -1){
//                        mDropGridView.sc.scrollToDown(afterViewY);
//                    }
//
//                    targetView.setVisibility(View.INVISIBLE);
//
//                    AnimationView animationView = new AnimationView();
//                    animationView.droped = targetView;
//
//                    Message msg = Message.obtain();
//                    msg.what = 3;
//                    msg.obj = animationView;
//                    mDropGridView.animationHandler.sendMessageDelayed(msg, 50);
//                }
//            });
//
//            return false;
//        }
//    };

	@Override
	public boolean onItemLongClick(final AdapterView<?> parent, final View view,
			final int position, long id) {

		isScrollAction = true;

		final int viewY = (int) view.getY();

		ViewGroup.LayoutParams params = parent.getLayoutParams();
		if(params != null){
			params.width = ViewGroup.LayoutParams.MATCH_PARENT;
			params.height = parent.getHeight() + view.getHeight();
			parent.setLayoutParams(params);
		}

		new Handler().post(new Runnable() {

			@Override
			public void run() {
//        ClipData data = ClipData.newPlainText("DragData", "");
				//선택된 뷰를 기준으로 드래그를 시작한다.
				final View targetView = (View) parent.getChildAt(position - parent.getFirstVisiblePosition());
				targetView.startDrag(null, new DragShadowBuilder(targetView), targetView, 0);

//        localView = targetView;
				int count = parent.getChildCount();

				//모든 뷰에 드래그 리스너를 셋팅한다
				for (int i = 0; i < count; i++) {

					View curr = parent.getChildAt(i);

					curr.setOnDragListener(DropGridView.this);

				}

				int afterViewY = (int) view.getY();
				afterViewY -= viewY;

				//스크롤시에 뷰를 미리 생성하기 하기 위하여 그리드뷰의 레이이아웃을 키운다.
				ViewGroup.LayoutParams params = parent.getLayoutParams();
				if(params != null){
					params.width = ViewGroup.LayoutParams.MATCH_PARENT;
					params.height = ViewGroup.LayoutParams.MATCH_PARENT;
					parent.setLayoutParams(params);
				}
				sc.scrollToDown(afterViewY);

				targetView.setVisibility(View.INVISIBLE);

				//선택한 뷰 핸들러에서 visiable 처리
				AnimationView animationView = new AnimationView();
				animationView.droped = targetView;

				Message msg = Message.obtain();
				msg.what = 3;
				msg.obj = animationView;
				animationHandler.sendMessageDelayed(msg, 50);
				//
			}
		});


		return false;
	}

	@Override
	public boolean onDrag(final View v, final DragEvent event) {
		boolean result = true;
        int action = event.getAction();

        animationHandler.removeMessages(3);

        switch (action) {
        case DragEvent.ACTION_DRAG_STARTED:
			break;
        case DragEvent.ACTION_DRAG_LOCATION:	//드래그 앤 드랍 중 지속적으로 반환
        {
    		if((v.getY()+ event.getY()) > getHeight() - 50){
    			sc.scrollToDown();
    		}else if((v.getY() + event.getY() < getY() + 50)){
    			sc.scrollToUp();
    		}
        }
        break;
        case DragEvent.ACTION_DRAG_ENTERED:	//아이템끼리 충돌이 있을 시
        {
        	final AnimationView animationView = new AnimationView();
        	final View droped = (View) event.getLocalState();
        	animationView.droped = droped;
        	animationView.view = v;

        	animationHandler.removeMessages(1);
        	Message msg = Message.obtain();
        	msg.obj = animationView;
        	msg.what = 0;
        	animationHandler.sendMessage(msg);

        	break;
        }
        case DragEvent.ACTION_DRAG_EXITED:	//취소 되었을 때
        	break;

        case DragEvent.ACTION_DROP:	//아이템이 성공적으로 드롭 되었을 때
        	{
                new Handler().postDelayed(new Runnable() {

                	@Override
                	public void run() {
						for (int i = 0; i < getChildCount(); i++) {
							getChildAt(i).setVisibility(View.VISIBLE);
						}
						isDrop = false;
						isScrollAction = false;
                	}
                }, ANIMATION_DURATION + 100);
        	}
        	break;

        case DragEvent.ACTION_DRAG_ENDED:	//이벤트가 종료 되었을 때(성공이든 취소든)
        	if(!event.getResult()){
        		final View droped = (View) event.getLocalState();
        		final View target = v;


        		new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						droped.setVisibility(View.VISIBLE);
						target.setVisibility(View.VISIBLE);
						isDrop = false;
						isScrollAction = false;
					}
				}, ANIMATION_DURATION + 100);
        	}
            break;
        }
        return result;
	}

	/**
	 * 애니메이션 이동 완료 후 실제 데이터 변경
	 * @param items
	 * @param index
	 */
	private synchronized void dataEdit(List<DropModel> items, int index){
		items.remove((DropModel)getDropModel());
		items.add(index, (DropModel)getDropModel());
		((BaseAdapter) getAdapter()).notifyDataSetChanged();
	}


	public DropModel getDropModel() {
		return dropModel;
	}

	public void setDropModel(DropModel dropModel) {
		this.dropModel = dropModel;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	/**
	 * 아이템들이 충돌 되었을 때 애니메이션을 시작한다.
	 * @param v
	 * @param droped
	 */
	private void startAnimation(View v, View droped){
		//선택된 아이템 저장
		DropModel dropItem = (DropModel) ((DropHolder) droped.getTag()).item;
		if(!isDrop){
			setDropModel(dropItem);
			isDrop = true;
		}

		//어댑터 가져오기
        final DropAdapter adapter = (DropAdapter) getAdapter();
        final List<DropModel> items = adapter.getItems();

        final View target = v;
        DropModel targetItem = (DropModel) ((DropHolder) target.getTag()).item;

        //애니메이션 대상 인덱스 설정
        final int index = items.indexOf(targetItem);
        final int endIndex = items.indexOf(getDropModel());

        int tempIndex = index;
        int tempEndIndex = endIndex;

        if(index > endIndex){
        	tempIndex = endIndex;
        	tempEndIndex = index;
        }

        //실제 애니메이션 설정(지정된 모든 뷰에)
        for (int i = tempIndex+1; i < tempEndIndex+1; i++) {

        	if(getChildAt(i - getFirstVisiblePosition()) != null){

        		int toX = getChildAt((i - getFirstVisiblePosition())).getWidth();
        		int toY = 0;
        		int fromX = 0;
            	int fromY = 0;

        		if(i % getNumColumns() == 0){
        			toX = -toX * (getNumColumns() -1);
        			toY = getChildAt((i - getFirstVisiblePosition())).getHeight();
        		}

        		int childId = (i - getFirstVisiblePosition())-1;
        		TranslateAnimation ta = new TranslateAnimation(fromX, toX, fromY, toY);
        		if(index > endIndex){
        			childId = (i - getFirstVisiblePosition());
        			ta = new TranslateAnimation(fromX, -toX, fromY, -toY);
        		}

        		if(i == tempEndIndex){
        			//애니메이션 리스너 설정
        			ta.setAnimationListener(new AnimationListener() {

        				@Override
        				public void onAnimationStart(Animation animation) {
        				}

        				@Override
        				public void onAnimationRepeat(Animation animation) {

        				}

        				@Override
        				public void onAnimationEnd(Animation animation) {
        					//애니메이션 완료 후 데이터 변경 및 뷰 정리 최적화
        					Runnable runnable = new Runnable() {

								@Override
								public void run() {
									if(Build.VERSION.SDK_INT < 16){
										if(index > endIndex && (index - endIndex) == 1){
											post(new Runnable() {

												@Override
												public void run() {
													dataEdit(items, index);
												}
											});
										}else {
											dataEdit(items, index);
										}
										for (int i = 0; i < getChildCount(); i++) {
											if(!getChildAt(i).equals(target)){
												getChildAt(i).setVisibility(View.VISIBLE);
											}
										}
									}else{
										dataEdit(items, index);
										for (int i = 0; i < getChildCount(); i++) {
											if(!getChildAt(i).equals(target)){
												getChildAt(i).setVisibility(View.VISIBLE);
											}
										}
									}
								}
							};

							new Handler().post(runnable);

        				}
        			});

    			}

        		ta.setDuration(ANIMATION_DURATION);
//        		ta.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.accelerate_interpolator));
        		if(getChildAt(childId) != null && ta != null){
        			getChildAt(childId).startAnimation(ta);
        		}
        		target.setVisibility(View.INVISIBLE);
        	}
        }
	}

	/**
	 * 애니메이션 핸들러
	 * 애니메이션 명령을 취소 할 수 있도록 시간차를 준다
	 */
	public Handler animationHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				//애니메이션 대기 - 명령 시작시 대기 한다.
				Message msg2 = Message.obtain();
				msg2.what = 1;
				msg2.obj = msg.obj;
				sendMessageDelayed(msg2, ANIMATION_DURATION);
				break;
			case 1:
				//애니메이션 실행
				AnimationView animationView = (AnimationView) msg.obj;
				startAnimation(animationView.view, animationView.droped);
				break;
			case 3:
				((AnimationView)msg.obj).droped.setVisibility(View.VISIBLE);
				break;
			}
		};
	};
	
}
