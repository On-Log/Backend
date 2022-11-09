package com.nanal.backend.domain.diary.service;

import com.nanal.backend.domain.diary.dto.KeywordDto;
import com.nanal.backend.domain.diary.dto.KeywordEmotionDto;
import com.nanal.backend.domain.diary.dto.ReqSaveDiaryDto;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.repository.KeywordRepository;
import com.nanal.backend.domain.oauth.repository.MemberRepository;
import com.nanal.backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final KeywordRepository keywordRepository;

    public void saveDiary(String email, ReqSaveDiaryDto reqSaveDiaryDto) {
        // email 로 유저정보 가져오기
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        // 일기 저장
        createDiary(member, reqSaveDiaryDto);
    }

    private void createDiary(Member member, ReqSaveDiaryDto reqSaveDiaryDto) {
        // Diary 생성에 필요한 Keyword 리스트 생성
        List<Keyword> keywords = new ArrayList<>();

        for (KeywordDto keywordDto : reqSaveDiaryDto.getKeywords()) {

            // Keyword 생성에 필요한 KeywordEmotion 리스트 생성
            List<KeywordEmotion> keywordEmotions = new ArrayList<>();

            // KeywordEmotion 리스트에 KeywordEmotion 생성하여 삽입.
            for(KeywordEmotionDto keywordEmotionDto : keywordDto.getKeywordEmotions()){
                KeywordEmotion keywordEmotion = KeywordEmotion.createKeywordEmotion(keywordEmotionDto.getEmotion());
                keywordEmotions.add(keywordEmotion);
            }

            // Keyword 리스트에 KeywordEmotion 리스트를 이용하여 생성한 Keyword 삽입.
            Keyword keyword = Keyword.createKeyword(keywordDto.getKeyword(), keywordEmotions);
            keywords.add(keyword);
        }

        // Keyword 리스트를 이용하여 Diary 생성
        Diary diary = Diary.createDiary(member, keywords, reqSaveDiaryDto.getContent(), reqSaveDiaryDto.getDate());

        // Diary 저장
        diaryRepository.save(diary);
    }
}
