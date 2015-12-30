# DragAndDropGridView

드래그 앤 드롭 그리드 뷰 입니다.

기본 그리드뷰를 상속 받아 아이템을 롱클릭하면 드래그 하여 원하는 위치에 드랍할 수 있는 뷰 입니다.

사용법은 아래와 같습니다.

DropModel : 어댑터에 들어가는 데이터로 해당 클래스를 상속 받아 데이터를 넣을수 있도록 합니다.

DropAdapter : 그리드뷰에 들어가는 어댑터로 해당 클래스를 상속 받아 뷰를 생성할 수 있도록 합니다.


  private List<DropModel> mList = new ArrayList<>();
  DropAdapter mDropAdapter = new NumberAdapter(this, mList);
  mDropGridView.setAdapter(mDropAdapter);
  
  //////////

    class NumberModel extends DropModel{

        private String mNumber;
        NumberModel(String number){
            mNumber = number;
        }

    }

    class NumberAdapter extends DropAdapter{

        NumberAdapter(Context context, List<DropModel> list){
            super(context, list);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DropHolder holder = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.adapter_number, null);
                holder = new DropHolder();
                convertView.setTag(holder);
            }else{
                holder = (DropHolder)convertView.getTag();
            }

            NumberModel model = (NumberModel)getItem(position);

            TextView textView = (TextView)convertView.findViewById(R.id.text1);
            textView.setText(model.mNumber);
            holder.item = model;

            return convertView;
        }
    }
