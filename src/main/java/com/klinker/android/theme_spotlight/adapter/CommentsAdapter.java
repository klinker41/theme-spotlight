/*
 * Copyright (C) 2014 Klinker Apps, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klinker.android.theme_spotlight.adapter;

import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gc.android.market.api.model.Market;
import com.klinker.android.theme_spotlight.R;
import com.klinker.android.theme_spotlight.activity.AuthActivity;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<Market.Comment> {

    private static final String EMOJI_STAR = "\u2B50";

    private final AuthActivity context;
    private final List<Market.Comment> items;

    static class ViewHolder {
        public TextView author;
        public TextView comment;
    }

    public CommentsAdapter(AuthActivity context, List<Market.Comment> items) {
        super(context, R.layout.review_comment);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.review_comment, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.author = (TextView) rowView.findViewById(R.id.name);
            viewHolder.comment = (TextView) rowView.findViewById(R.id.review);

            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        final Market.Comment item = items.get(position);

        holder.author.setText(parseName(item.getAuthorName(), item.getRating()));
        holder.comment.setText(parseComment(item.getText()));

        return rowView;
    }

    private String parseName(String name, int rating) {
        name += " - ";

        for (int i = 0; i < rating; i++) {
            name += EMOJI_STAR;
        }

        return name;
    }

    // parse the comment and display it formatted nicely
    private Spanned parseComment(String comment) {
        String[] parts = comment.split("\t");

        if (parts.length == 1) { // there is no title
            return new SpannedString(comment);
        } else {
            return Html.fromHtml("<font color=\"#212121\">" + parts[0] + " - </font>" + parts[1]);
        }
    }
}