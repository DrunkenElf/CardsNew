package com.ilnur.cardsnew.cat_binders

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ilnur.cardsnew.R
import tellh.com.recyclertreeview_lib.LayoutItemType
import tellh.com.recyclertreeview_lib.TreeNode
import tellh.com.recyclertreeview_lib.TreeViewBinder




class CatBtnBinder : TreeViewBinder<CatBtnBinder.ViewHolder>() {
    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun bindView(holder: ViewHolder, position: Int, node: TreeNode<*>) {
        val fileNode: CatHead = node.content as CatHead
        holder.tvName.text = fileNode.title
    }

    override fun getLayoutId(): Int {
        return R.layout.cat_btn
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var tvName: TextView

        init {
            tvName = rootView.findViewById<View>(R.id.cat_name) as TextView
        }
    }
}

class CatHeadBinder : TreeViewBinder<CatHeadBinder.ViewHolder>() {
    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun bindView(holder: ViewHolder, position: Int, node: TreeNode<*>) {
        val fileNode: CatHead = node.content as CatHead
        holder.tvName.text = fileNode.title
    }

    override fun getLayoutId(): Int {
        return R.layout.cat_header
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var tvName: TextView

        init {
            tvName = rootView.findViewById<View>(R.id.cat_name) as TextView
        }
    }
}

class CatBtn(val subj: String, val title: String, val id: Int, val context: Context) :
    LayoutItemType {

    override fun getLayoutId(): Int {
        return R.layout.cat_btn
    }
}

class CatHead(val title: String) : LayoutItemType {

    override fun getLayoutId(): Int {
        return R.layout.cat_header
    }
}