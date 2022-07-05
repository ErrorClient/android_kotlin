package com.android.ututapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.data.models.StatusLoading
import com.android.ututapp.R
import com.android.ututapp.databinding.FragmentTableBinding
import com.android.ututapp.presentation.vm.ImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TableFragment : Fragment() {

    private var _binding: FragmentTableBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TableAdapter
    private lateinit var recyclerview: RecyclerView
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var networkButton: Button
    private lateinit var tableText: TextView

    private val imageListViewModel by viewModel<ImageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTableBinding.inflate(inflater, container, false)
        nestedScrollView = binding.nestedScroll
        networkButton = binding.networkButton.apply { isVisible = false }
        tableText = binding.tableText

        /***
         * Узнаем сколько картинок 100х100 поместится по вертикали, горизонтали и на всем экране
         */
        imageListViewModel.getSize(requireContext())
        val countHorizontal = imageListViewModel.countHorizontal

        recyclerview = binding.recyclerView
            .apply {
                layoutManager = GridLayoutManager(context, countHorizontal)
            }

        adapter = TableAdapter(imageList = emptyList(), context = this)

        recyclerview.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /***
         * Наблюдаем за статусом загрузки, пока наша вью видна
         */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            imageListViewModel.status.collect { status ->
                when (status) {
                    StatusLoading.Error -> networkButton.isVisible = true
                    StatusLoading.Loading -> networkButton.isVisible = false
                    StatusLoading.Success -> networkButton.isVisible = false
                }
            }
        }

        /***
         * Наблюдатель за БД
         * Если появились новые данные, добавляем их в адаптер
         * Самую первую загрузку инициирую тут, но должно быть место лучше :)
         * 2 загрузки одновременно существовать не могут, так что это работает
         */
        imageListViewModel.imageListLiveData.observe(
            viewLifecycleOwner
        ) { image ->
            image?.let {
                if (image.isEmpty()) {
                    imageListViewModel.addImage(adapterSize = adapter.itemCount)
                }
                adapter.addItems(newImageList = image)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->

                val diffMeasuredHeight = v.getChildAt(0).measuredHeight - v.measuredHeight

                /***
                 * Догружаем недостающие по горизонтали + еще 1 строка
                 */
                if (scrollY == diffMeasuredHeight) {
                    imageListViewModel.addImage(adapterSize = adapter.itemCount)
                }
            })

        adapter.setOnImageClickListener(
            object : TableAdapter.OnImageClickListener {

                override fun onImageClick(position: Int) {

                    /***
                     * Передаем выбранную картинку на второй фрагмент
                     * Используем анимацию
                     * Тут БАГ, если открыть гифку, то именно гифкой она будет со втогоро раза :(
                     */
                    val urlImage = adapter.imageList[position].url
                    val action =
                        TableFragmentDirections.actionTableFragmentToFullSizeFragment(urlImage)

                    val extras = FragmentNavigatorExtras(
                        adapter.clickedImageView!! to getString(R.string.transition_name)
                    )

                    findNavController().navigate(action, extras)
                }
            })

        networkButton.setOnClickListener {
            /***
             * Если есть интернет, то грузим недостающие картинки
             */
            imageListViewModel.addImage(adapterSize = adapter.itemCount)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}