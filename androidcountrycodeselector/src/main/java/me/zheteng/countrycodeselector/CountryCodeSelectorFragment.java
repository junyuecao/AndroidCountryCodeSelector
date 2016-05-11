package me.zheteng.countrycodeselector;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Country Code Selector Fragment
 */
public class CountryCodeSelectorFragment extends DialogFragment implements TextWatcher {
    private static final String TAG = "CCSFragment";

    private List<Country> mCountryList;
    private RecyclerView mRecyclerView;
    private CountryListAdapter mAdapter;
    private int mThemeColor;

    private OnCountrySelectListener mOnCountrySelectListener;
    private int mType;

    public static CountryCodeSelectorFragment newInstance(Intent intent) {

        Bundle args = intent.getExtras();
        CountryCodeSelectorFragment fragment = new CountryCodeSelectorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mThemeColor = arguments.getInt(PhoneInputView.EXTRA_THEME_COLOR,
                ContextCompat.getColor(getActivity(), R.color.ccs_default_color));
        mType = arguments.getInt(PhoneInputView.EXTRA_SELECTOR_TYPE);
        mCountryList = CountryListManager.from(getActivity()).getList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_code_selector, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CountryListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setList(mCountryList);

        if (mType == PhoneInputView.SELECTOR_TYPE_DIALOG) {
            getDialog().setTitle(R.string.choose_a_country);

            int width = getResources().getDimensionPixelSize(R.dimen.ccs_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.ccs_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mAdapter.setFilter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public OnCountrySelectListener getOnCountrySelectListener() {
        return mOnCountrySelectListener;
    }

    public void setOnCountrySelectListener(OnCountrySelectListener onCountrySelectListener) {
        mOnCountrySelectListener = onCountrySelectListener;
    }

    /**
     * Country list adapter
     */
    public class CountryListAdapter extends RecyclerView.Adapter {
        public static final int VIEW_TYPE_SEARCH = 1;
        public static final int VIEW_TYPE_ITEM = 2;
        List<Country> mList;
        List<Country> mFilteredList = new ArrayList<>();
        /**
         * active position in mList
         */
        int mActive;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view;
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case VIEW_TYPE_ITEM:
                    view = inflater.inflate(R.layout.item_country, parent, false);
                    holder = new CountryViewHolder(view);
                    break;
                case VIEW_TYPE_SEARCH:
                    view = inflater.inflate(R.layout.item_search, parent, false);
                    holder = new SearchViewHolder(view);
                    break;
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CountryViewHolder) {
                CountryViewHolder viewHolder = (CountryViewHolder) holder;
                Country country = mFilteredList.get(position - 1);
                viewHolder.itemView.setTag(country);
                viewHolder.countryName.setText(country.getName());
                viewHolder.countryNameEnglish.setText(country.getNameInEnglish());
                viewHolder.countryCode.setText("+" + country.getCode());

                if (TextUtils.equals(country.getName(), country.getNameInEnglish())) {
                    viewHolder.countryNameEnglish.setVisibility(View.GONE);
                } else {
                    viewHolder.countryNameEnglish.setVisibility(View.VISIBLE);
                }

                if (position == mActive) {
                    viewHolder.active.setVisibility(View.VISIBLE);
                    viewHolder.countryName.setTextColor(mThemeColor);
                } else {
                    viewHolder.active.setVisibility(View.INVISIBLE);
                    viewHolder.countryName
                            .setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
                }
            } else if (holder instanceof SearchViewHolder) {
                SearchViewHolder viewHolder = (SearchViewHolder) holder;

                viewHolder.search.requestFocus();
            }
        }

        @Override
        public int getItemCount() {
            return getListSize() + 1;
        }

        public void setList(List<Country> list) {
            mList = list;
            if (mList != null) {
                mFilteredList.addAll(mList);
            }
            notifyDataSetChanged();
        }

        public void setFilter(String s) {
            mFilteredList.clear();
            for (Country country : mList) {
                if (TextUtils.isEmpty(s) // filter is empty
                        || country.getCode().toLowerCase().contains(s.toLowerCase())
                        || country.getName().toLowerCase().contains(s.toLowerCase())
                        || country.getNameInEnglish().toLowerCase().contains(s.toLowerCase())) {
                    mFilteredList.add(country);
                }
            }
            notifyDataSetChanged();
        }

        private int getListSize() {
            return mFilteredList == null ? 0 : mFilteredList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_SEARCH;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView countryName;
        TextView countryNameEnglish;
        TextView countryCode;
        ImageView active;

        public CountryViewHolder(View itemView) {
            super(itemView);

            countryName = (TextView) itemView.findViewById(R.id.country_name);
            countryNameEnglish = (TextView) itemView.findViewById(R.id.country_name_english);
            countryCode = (TextView) itemView.findViewById(R.id.county_code);
            active = (ImageView) itemView.findViewById(R.id.active);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Country country = (Country) itemView.getTag();

            Intent intent = new Intent(PhoneInputView.ACTION_SEND_RESULT);
            intent.putExtra(PhoneInputView.EXTRA_COUNTRY, country);
            getActivity().sendBroadcast(intent);

            if (mOnCountrySelectListener != null) {
                mOnCountrySelectListener.onCountrySelect(country);
            }
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        EditText search;

        public SearchViewHolder(View itemView) {
            super(itemView);

            search = (EditText) itemView.findViewById(R.id.search);
            search.getBackground().setColorFilter(mThemeColor, PorterDuff.Mode.SRC_ATOP);
            search.addTextChangedListener(CountryCodeSelectorFragment.this);
        }
    }

    public interface OnCountrySelectListener {
        void onCountrySelect(Country country);
    }
}
