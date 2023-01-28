DELETE FROM emotion;
DELETE FROM question;
DELETE FROM extra_question;

INSERT INTO emotion(emotion_id, emotion) VALUES (1, "행복");
INSERT INTO emotion(emotion_id, emotion) VALUES (2, "여유");
INSERT INTO emotion(emotion_id, emotion) VALUES (3, "안심");
INSERT INTO emotion(emotion_id, emotion) VALUES (4, "슬픔");
INSERT INTO emotion(emotion_id, emotion) VALUES (5, "복잡");
INSERT INTO emotion(emotion_id, emotion) VALUES (6, "즐거움");
INSERT INTO emotion(emotion_id, emotion) VALUES (7, "의욕");
INSERT INTO emotion(emotion_id, emotion) VALUES (8, "쏘쏘");
INSERT INTO emotion(emotion_id, emotion) VALUES (9, "아쉬움");
INSERT INTO emotion(emotion_id, emotion) VALUES (10, "화남");
INSERT INTO emotion(emotion_id, emotion) VALUES (11, "기대");
INSERT INTO emotion(emotion_id, emotion) VALUES (12, "놀람");
INSERT INTO emotion(emotion_id, emotion) VALUES (13, "외로움");
INSERT INTO emotion(emotion_id, emotion) VALUES (14, "짜증");
INSERT INTO emotion(emotion_id, emotion) VALUES (15, "힘듦");
INSERT INTO emotion(emotion_id, emotion) VALUES (16, "뿌듯");
INSERT INTO emotion(emotion_id, emotion) VALUES (17, "상쾌");
INSERT INTO emotion(emotion_id, emotion) VALUES (18, "불안");
INSERT INTO emotion(emotion_id, emotion) VALUES (19, "부담");
INSERT INTO emotion(emotion_id, emotion) VALUES (20, "피곤");

INSERT INTO question(question_id, content, help, goal_id) VALUES (1, "이번주 나의 모습은 어땠나요?", "이번주 나의 모습을 묘사하기 어려우신가요? 가장 먼저 떠오르는 내 모습, 혹은 가장 자주 보였던 나의 모습을 떠올려보세요.", 1);
INSERT INTO question(question_id, content, help, goal_id) VALUES (2, "다른 내 모습도 들려줄래요? 이번주에 찾은 의외의 내 모습이 있다면요?", "우리의 일주일은 한가지 색만으로 이루어져있지 않아요! 가장 사소한 일부터 차근 차근 생각해보세요.", 1);
INSERT INTO question(question_id, content, help, goal_id) VALUES (3, "다음주에도 유지하고 싶은 나의 모습이 있을까요? 혹은 새롭게 찾고 싶은 나의 모습이 있다면 무엇인가요?", "그 모습의 나는 구체적으로 어떤 행동을 하게 될까요? 그 모습이 되려면 어떤 노력을 해야할지도 생각해봅시다.", 1);
INSERT INTO question(question_id, content, help, goal_id) VALUES (4, "이번주 중 성취를 바라고 한 행동이 있나요? 구체적인 목표가 무엇인가요?", "대단하거나 인상적인 성취가 아니어도 괜찮아요. 작더라도, 조금이라도 의미었던 성취를 들려주세요!", 2);
INSERT INTO question(question_id, content, help, goal_id) VALUES (5, "목표를 이루기 위해 어떤 행동을 했나요? 당신의 일주일 중 얼마의 시간을 들였나요?", "목표를 이루기 위한 직접적이고, 큰 행동일 필요는 없어요. 목표를 이루기 위해 계획하고, 조금이라도 대비한 모든 순간이 ''목표를 이루기 위한 행동''입니다.
시간을 구체적으로 가늠하기 어렵다면 많이/적당히/조금과 같은 주관적인 기준을 사용하셔도 좋아요.", 2);
INSERT INTO question(question_id, content, help, goal_id) VALUES (6, "어떤 것이 좋아지면 더 보람있는 일주일을 보낼 수 있을까요?", "앞서 말해주신 목표를 더 잘 이루기 위한 방법에 대해서 생각해봐도 좋고, 아예 새로운 목표와 새로운 행위에 대해서 이야기해주셔도 좋아요!", 2);
INSERT INTO question(question_id, content, help, goal_id) VALUES (7, "이번주에 가장 많이 느꼈던 감정은 무엇인가요?", "감정을 한가지만 고르기 힘들다면 여러가지를 골라주세요. 그 중 어떤 감정들이 가장 자주 있었는지, 강력했는지, 혹은 인상적이었는지도 알려주세요.", 3);
INSERT INTO question(question_id, content, help, goal_id) VALUES (8, "해당 감정이 많이 들었던 이유가 있나요? 당시의 주위 상황을 떠올리며 감정의 원인을 고민해보세요! ", "감정의 원인은 누구나 파악하기 어려운 것이에요. 잘 떠오르지 않아도 걱정하지 마세요! 다음의 것들을 떠올리며 차근차근 생각해보아요.
- 감정이 들었던 순간에 있었던 장소와 시간
- 감정이 들었던 순간 주위의 사람들, 그들의 반응
- 감정이 들었던 순간 내가 지었던 표정
- 감정이 들었던 직후 내가 했던 말", 3);
INSERT INTO question(question_id, content, help, goal_id) VALUES (9, "다음주는 어떤 감정으로 살고 싶어요?", "그 감정을 가지려면 어떤 행동과 생각을 해야할지도 생각해보아요.", 3);
INSERT INTO question(question_id, content, help, goal_id) VALUES (10, "이번주에 나를 속상하게 하거나, 웃게 한 사람이 있었나요? 지금 떠오르는 사람에 대해 알려주세요!", "속상했던 감정, 혹은 즐거웠던 감정이 크지 않았어도 괜찮아요. 직접 만난 사람이 아니어도 괜찮고, 아예 모르는 사람이어도 좋아요.", 4);
INSERT INTO question(question_id, content, help, goal_id) VALUES (11, "그 사람은 당신에게 어떤 영향을 주는 사람인가요? 이 관계를 통해 배운 점은 무엇인가요?", "배운 점이 잘 떠오르지 않는다면 이 사람에 대해 든 생각을 적어보세요. 왜 이런 생각이 들었을까요? 이 생각이 앞으로의 관계에 어떤 영향을 미치게 될까요?", 4);
INSERT INTO question(question_id, content, help, goal_id) VALUES (12, "나는 그 사람과의 관계를 어떻게 맺고 싶나요?", "어떤 관계인지 설명하기 어렵다면 그 사람과 어떤 교류를 원하는지부터 생각해보세요. 그 사람에게 어떤 말을 듣고 싶나요? 그 사람과 무엇을 하고 싶나요? 그 사람이 나를 어떻게 생각하면 좋을까요?", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (1, "추가질문1-1", "추가질문1 도움말", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (2, "추가질문1-2", "추가질문2 도움말", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (3, "추가질문1-3", "추가질문3 도움말", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (4, "추가질문1-4", "추가질문4 도움말", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (5, "추가질문1-5", "추가질문5 도움말", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (6, "추가질문1-6", "추가질문6 도움말", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (7, "추가질문2-1", "추가질문1 도움말", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (8, "추가질문2-2", "추가질문2 도움말", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (9, "추가질문2-3", "추가질문3 도움말", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (10, "추가질문2-4", "추가질문4 도움말", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (11, "추가질문2-5", "추가질문5 도움말", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (12, "추가질문2-6", "추가질문6 도움말", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (13, "추가질문3-1", "추가질문1 도움말", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (14, "추가질문3-2", "추가질문2 도움말", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (15, "추가질문3-3", "추가질문3 도움말", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (16, "추가질문3-4", "추가질문4 도움말", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (17, "추가질문3-5", "추가질문5 도움말", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (18, "추가질문3-6", "추가질문6 도움말", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (19, "추가질문4-1", "추가질문1 도움말", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (20, "추가질문4-2", "추가질문2 도움말", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (21, "추가질문4-3", "추가질문3 도움말", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (22, "추가질문4-4", "추가질문4 도움말", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (23, "추가질문4-5", "추가질문5 도움말", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (24, "추가질문4-6", "추가질문6 도움말", 4);