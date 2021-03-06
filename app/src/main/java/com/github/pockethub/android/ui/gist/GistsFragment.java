/*
 * Copyright (c) 2015 PocketHub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pockethub.android.ui.gist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.github.pockethub.android.R;
import com.github.pockethub.android.core.gist.GistStore;
import com.github.pockethub.android.ui.PagedItemFragment;
import com.github.pockethub.android.ui.item.gist.GistItem;
import com.github.pockethub.android.util.AvatarLoader;
import com.meisolsson.githubsdk.model.Gist;
import com.xwray.groupie.Item;

import javax.inject.Inject;

import static com.github.pockethub.android.RequestCodes.GIST_CREATE;
import static com.github.pockethub.android.RequestCodes.GIST_VIEW;

/**
 * Fragment to display a list of Gists
 */
public abstract class GistsFragment extends PagedItemFragment<Gist> {

    /**
     * Avatar loader
     */
    @Inject
    protected AvatarLoader avatars;

    /**
     * Gist store
     */
    @Inject
    protected GistStore store;

    @Override
    public void onItemClick(@NonNull Item item, @NonNull View view) {
        int position = getListAdapter().getAdapterPosition(item);
        startActivityForResult(GistsViewActivity.createIntent(items, position), GIST_VIEW);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_gists);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isAdded()) {
            return false;
        }
        switch (item.getItemId()) {
        case R.id.m_create:
            startActivityForResult(new Intent(getActivity(),
                    CreateGistActivity.class), GIST_CREATE);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GIST_VIEW || requestCode == GIST_CREATE) {
            notifyDataSetChanged();
            forceRefresh();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getErrorMessage() {
        return R.string.error_gists_load;
    }

    @Override
    protected int getLoadingMessage() {
        return R.string.loading_gists;
    }

    @Override
    protected Item createItem(Gist item) {
        return new GistItem(avatars, item);
    }
}
