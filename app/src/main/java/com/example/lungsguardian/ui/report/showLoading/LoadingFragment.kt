package com.example.lungsguardian.ui.report.showLoading

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentLoadingBinding
import com.example.lungsguardian.utils.ARABIC
import com.example.lungsguardian.utils.IMAGE_FILE
import com.example.lungsguardian.utils.IMAGE_URI
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Locale

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    private var _binding :FragmentLoadingBinding ?= null
    private val binding get() = _binding!!
    lateinit var  englishArabicTranslator : Translator
    lateinit var englishCaption :String
    lateinit var language :String
    private val loadingViewModel : LoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentLoadingBinding.bind(view)
        checkLanguage()
        val uriImage = requireActivity().intent?.extras?.get(IMAGE_URI) as Uri
        val fileImage = requireActivity().intent?.extras?.get(IMAGE_FILE)
        sendImageToModel(fileImage as File)
        observers(uriImage )
    }

    private fun checkLanguage() {
        val currentLocale: Locale = Locale.getDefault()
        language = currentLocale.language
    }

    private fun prepareTranslateModel(receivedCaption:String,uriImage: Uri) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.ARABIC)
            .build()
        englishArabicTranslator = Translation.getClient(options)

        englishArabicTranslator.downloadModelIfNeeded().addOnSuccessListener {
            translateText(receivedCaption,uriImage)

        }.addOnFailureListener {
            findNavController().navigate(
                LoadingFragmentDirections.actionLoadingFragmentToReportFragment(englishCaption,uriImage,""))
        }
    }

    private fun translateText(receivedCaption:String,uriImage: Uri) {
        englishArabicTranslator.translate(receivedCaption).addOnSuccessListener {
            findNavController().navigate(
                LoadingFragmentDirections.actionLoadingFragmentToReportFragment(englishCaption,uriImage,it))
        }.addOnFailureListener {
            findNavController().navigate(
                LoadingFragmentDirections.actionLoadingFragmentToReportFragment(englishCaption,uriImage,""))
        }
    }

    private fun sendImageToModel(fileImage: File) {
        loadingViewModel.sendImageToModel(fileImage)
    }

    private fun observers(uriImage: Uri) {
        loadingViewModel.modelValidate.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        loadingViewModel.responseLiveData.observe(viewLifecycleOwner){
            englishCaption=it.body()?.caption!!
            if (language == ARABIC){
            prepareTranslateModel(englishCaption,uriImage)}
            else {
                findNavController().navigate(
                    LoadingFragmentDirections.actionLoadingFragmentToReportFragment(englishCaption,uriImage,""))
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}