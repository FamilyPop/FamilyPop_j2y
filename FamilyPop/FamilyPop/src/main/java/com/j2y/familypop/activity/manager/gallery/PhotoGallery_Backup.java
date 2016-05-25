//package com.j2y.familypop.activity.manager.gallery;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import com.nclab.familypop.R;
//
//import java.io.File;
//import java.util.ArrayList;
//
///**
// * Created by J2YSoft_Programer on 2016-05-03.
// */
//public class PhotoGallery_Backup implements ListView.OnScrollListener, GridView.OnItemClickListener
//{
//    boolean _busy = false;
//    ProgressDialog _loagind_Dialog;
//    GridView _gridView;
//    ImageAdapter _imageAdapter;
//    ArrayList<ImageInfo> _thumb_imageList;
//    Context _context;
//    Activity _activity;
//
//    public PhotoGallery_Backup(Activity activity, GridView gridview)
//    {
//        _thumb_imageList = new ArrayList<ImageInfo>();
//        _gridView = gridview;
//        _context = activity;
//        _activity = activity;
//
//        _gridView.setOnScrollListener(this);
//        _gridView.setOnItemClickListener(this);
//
//        new DoFindImageList().execute();
//    }
//
//
//    private long findThumbList()
//    {
//        long returnValue = 0;
//
//        //Select 하고자 하는 컬럼
//        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
//
//        //쿼리 수행
//        Cursor imageCursor = _context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
//
//        if(imageCursor != null && imageCursor.getCount() > 0)
//        {
//            //컬럼 인덱스
//            int imageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
//            int imageDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
//
//            //커서에서 이미지의 ID와 경로명을 가져와서 Thumb이미지 모델 클래스를 생성하여 리스트에 더해줌
//            while(imageCursor.moveToNext())
//            {
//                ImageInfo thumbImage = new ImageInfo();
//
//                thumbImage.SetId(imageCursor.getString(imageIDCol));
//                thumbImage.SetData(imageCursor.getString(imageDataCol));
//                thumbImage.SetCheckedState(false);
//
//                _thumb_imageList.add(thumbImage);
//                returnValue++;
//            }
//        }
//
//        imageCursor.close();
//        return returnValue;
//
//    }
//
//    private void updateUI()
//    {
//        _imageAdapter = new ImageAdapter(_context, R.layout.gridview_item_image, _thumb_imageList);
//        _gridView.setAdapter(_imageAdapter);
//    }
//
//    // 아이템 체크시 현재 체크상태를 가져와서 반대로 변경(true -> false, false -> true)시키고
//    // 그 결과를 다시 ArrayList의 같은 위치에 담아준다
//    // 그리고 어댑터의 notifyDataSetChanged() 메서드를 호출하면 리스트가 현재 보이는
//    // 부분의 화면을 다시 그리기 시작하는데(getView 호출) 이러면서 변경된 체크상태를
//    // 파악하여 체크박스에 체크/언체크를 처리한다.
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//    {
//        ImageAdapter adapter = (ImageAdapter) parent.getAdapter();
//        ImageInfo rowData = (ImageInfo)adapter.getItem(position);
//        boolean curCheckState = rowData.GetCheckedState();
//
//        rowData.SetCheckedState(!curCheckState);
//
//        _thumb_imageList.set(position,rowData);
//        adapter.notifyDataSetChanged();
//    }
//
//    //스크롤 상태를 판단, 스크롤 상태가 IDLE인 경우 (_busy == false) 에만 이미지 어댑터의 getView에서 이미지 출력
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState)
//    {
//        switch (scrollState)
//        {
//            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                _busy = false;
//                _imageAdapter.notifyDataSetChanged();
//                break;
//            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                _busy = true;
//                break;
//            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                _busy = true;
//                break;
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
//    { }
//
//    public ArrayList<ImageInfo> GetImageList()
//    {
//        if(_thumb_imageList.size() <= 0 )
//            return null;
//
//        ArrayList<ImageInfo> imageList = new ArrayList<ImageInfo>();
//
//        for(int i = 0; i < _thumb_imageList.size(); i++)
//        {
//            if(_thumb_imageList.get(i).GetCheckedState())
//            {
//                imageList.add(_thumb_imageList.get(i));
//            }
//        }
//
//        return imageList;
//    }
//
//
//
//    static class ImageViewHolder
//    {
//        ImageView _imageView;
//        CheckBox _checkBox;
//    }
//
//
//    private class ImageAdapter extends BaseAdapter
//    {
//        private Context _Adaptercontext;
//        private int _cell_layout;
//        private LayoutInflater _line_flater;
//        private ArrayList<ImageInfo> _thumbInfo_List;
//
//        @SuppressWarnings("unchecked")
//        private ImageCache _cache; //캐쉬
//
//        public ImageAdapter(Context c, int cellLayout, ArrayList<ImageInfo> thumbList)
//        {
//            _Adaptercontext = c;
//            _cell_layout = cellLayout;
//            _thumbInfo_List = thumbList;
//
//            _line_flater = (LayoutInflater) _Adaptercontext.getSystemService(_Adaptercontext.LAYOUT_INFLATER_SERVICE);
//
//            //캐쉬 초기화 : 캐쉬의 최대 보관 크기 30개
//            _cache = new ImageCache<String,Bitmap>(30);
//        }
//
//
//        @Override
//        public int getCount()
//        {
//            return _thumbInfo_List.size();
//        }
//
//        @Override
//        public Object getItem(int position)
//        {
//            return _thumbInfo_List.get(position);
//        }
//
//        @Override
//        public long getItemId(int position)
//        {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            int a = 5;
//
//            if(convertView == null)
//            {
//                convertView = _line_flater.inflate(_cell_layout,parent,false);
//                ImageViewHolder holder = new ImageViewHolder();
//
//                holder._imageView = (ImageView) convertView.findViewById(R.id.image_view);
//                holder._checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
//
//                convertView.setTag(holder);
//            }
//
//            final ImageViewHolder holder = (ImageViewHolder) convertView.getTag();
//
//            if(((ImageInfo) _thumbInfo_List.get(position)).GetCheckedState())
//            {
//                holder._checkBox.setChecked(true);
//            }
//            else
//                holder._checkBox.setChecked(false);
//            if(!_busy) {
//                try {
//                    String path = ((ImageInfo) _thumbInfo_List.get(position)).GetData();
//
//                    Bitmap bmp = (Bitmap) _cache.get(path);
//
//
//                    //bmp 객체를 캐쉬에서 가져왔다면 그것을 그대로 사용
//                    if(bmp != null)
//                    {
//                        holder._imageView.setImageBitmap(bmp);
//                    }
//
//                    //캐쉬에 내용이 없다면 실제 파일로 Bitmap 객체를 만들어 주고 이름을 캐쉬에 넣는다.
//                    else
//                    {
//                        BitmapFactory.Options option = new BitmapFactory.Options();
//                        if (new File(path).length() > 100000)
//                            option.inSampleSize = 10;
//                        else
//                            option.inSampleSize = 2;
//
//                        bmp = BitmapFactory.decodeFile(path, option);
//                        holder._imageView.setImageBitmap(bmp);
//
//                        _cache.put(path,bmp); //캐쉬에 넣어줌
//                    }
//                    holder._imageView.setVisibility(View.VISIBLE);
//                    _activity.setProgressBarIndeterminateVisibility(false);
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                    _activity.setProgressBarIndeterminateVisibility(false);
//                }
//            }
//            else
//            {
//                _activity.setProgressBarIndeterminateVisibility(true);
//                holder._imageView.setVisibility(View.INVISIBLE);
//            }
//            convertView.setPadding(8,8,8,8);
//            convertView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
//            return convertView;
//        }
//
//
//    }
//
//
//    private class DoFindImageList extends AsyncTask<String, Integer, Long>
//    {
//        @Override
//        protected void onPreExecute() {
//            _loagind_Dialog = ProgressDialog.show(_activity, null, "로딩중...",true,true);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Long doInBackground(String... params) {
//            long returnValue = 0;
//            returnValue = findThumbList();
//            return returnValue;
//        }
//
//        @Override
//        protected void onPostExecute(Long aLong) {
//            updateUI();
//            _loagind_Dialog.dismiss();
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//    }
//}
