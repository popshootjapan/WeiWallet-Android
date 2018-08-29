package com.wei.weiwallet.backup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.wei.weiwallet.R
import com.wei.weiwallet.databinding.FragmentBackupBinding
import com.wei.weiwallet.main.MainFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_backup.*
import javax.inject.Inject


class BackupFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: BackupViewModel

    private lateinit var binding: FragmentBackupBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentBackupBinding>(
            inflater, R.layout.fragment_backup, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(
            view: View?,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        // TODO DRY
        (0 until left.childCount)
                .map { Pair(left.getChildAt(it), viewModel.mnemonic().split(" ")[it]) }
                .filter { it.first is TextView }
                .forEach { (it.first as TextView).text = it.second }

        (0 until right.childCount)
                .map { Pair(right.getChildAt(it), viewModel.mnemonic().split(" ")[it + 6]) }
                .filter { it.first is TextView }
                .forEach { (it.first as TextView).text = it.second }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.confirm, menu)
        val item = menu?.findItem(R.id.action_confirm)
        item?.setActionView(R.layout.view_confirm)
        item?.actionView?.setOnClickListener {
            viewModel.putIsAlreadyBackup()
            showMainFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            activity.finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showMainFragment() {
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.container, MainFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
