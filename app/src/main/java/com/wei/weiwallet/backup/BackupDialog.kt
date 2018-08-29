package com.wei.weiwallet.backup

import android.support.v4.app.DialogFragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.DialogBackupBinding
import kotlinx.android.synthetic.main.dialog_backup.*

class BackupDialog : DialogFragment() {
    var closeCallback: (() -> Unit)? = null
    var backupCallback: (() -> Unit)? = null

    private lateinit var binding: DialogBackupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = DataBindingUtil.inflate<DialogBackupBinding>(inflater, R.layout.dialog_backup, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backup.setOnClickListener { backupCallback?.invoke() }
        close.setOnClickListener { closeCallback?.invoke() }
    }
}
