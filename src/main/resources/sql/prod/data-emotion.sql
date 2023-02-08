DROP PROCEDURE IF EXISTS INIT_EMOTION;

DELIMITER $$
CREATE PROCEDURE INIT_EMOTION()
BEGIN

    DECLARE cnt INT;
    SELECT COUNT(*) INTO cnt FROM emotion;

    IF cnt = 0 THEN
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (1, "행복", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at ) VALUES (2, "여유", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (4, "슬픔", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (5, "복잡", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (6, "즐거움", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (7, "의욕", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (8, "쏘쏘", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (3, "안심", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (9, "아쉬움", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (10, "화남", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (11, "기대", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (12, "놀람", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (13, "외로움", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (14, "짜증", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (15, "힘듦", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (16, "뿌듯", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (17, "상쾌", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (18, "불안", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (19, "부담", now(), now());
        INSERT INTO emotion(emotion_id, emotion, created_at, updated_at) VALUES (20, "피곤", now(), now());
    END IF;
END $$;

-- DB에 만들어둔 Procedure 실행
CALL INIT_EMOTION();