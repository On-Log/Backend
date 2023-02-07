
-- DB에 만들어둔 Procedure 실행
CALL INIT_EMOTION();

DELETE FROM question;
DELETE FROM extra_question;

INSERT INTO question(question_id, content, help, goal_id) VALUES (1, "이번주 나의 모습은 어땠나요?", "한가지 모습을 고르기 어렵다면, 가장 인상적인 (혹은 가장 기억에 남는) 모습에 대해 들려주세요.", 1);
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

INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (1, "나를 가장 나답게 만드는 것은 무엇인가요?", "'나다운 것'이 무엇인지 생각해보아요.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (2, "이번주를 마무리하며, 나에게 하고 싶은 말 한 마디를 건네주세요.", "생각이 말과 글로 구체화될 때 그 힘이 커진다고 하죠. 스스로에게 건네는 말도 마찬가지랍니다.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (3, "일주일 중, 온전히 나에게만 집중했던 시간은 얼마였나요? 그 시간을 어떻게 사용했나요?", "나의 움직임과 나의 나아감, 나의 것에만 푹 빠져있던 순간이 있었나요? 자아에 관한 심오한 주제를 말하는 게 아니에요. 그저 당신이 원했던 몰입의 순간이었다면, 무엇이든 좋아요.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (4, "내가 생각하는 최고의 하루는 어떤 하루인가요?", "그것만으로 충만했던 하루가 있나요? 그런 나날들에는 어떤 공통점이 있나요?", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (5, "최근에 내가 가장 아끼던 것은 무엇인가요?", "나의 애장품을 소개해주세요. 그것과 함께하는 나는 어떤 모습인가요? 어떤 감정을 느꼈나요?", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (6, "나의 별명을 직접 지어보는 시간입니다. 당신은 어떻게 불리우고 싶나요?", "조금 오글거려도 괜찮아요. 스스로 강조하고 싶은 나의 매력이 있다면, 이번 기회에 별명으로 지어보아요.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (7, "내 자신감의 원천은 무엇인가요?", "당신은 어떤 순간에 안정감, 그리고 당당함을 느끼나요? 당신의 근거있는 자신감에 대해 들어보고 싶어요.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (8, "당신을 복제해놓은 5명이 일상을 대신 살아주고 있습니다. 이때 당신이 직접 하고 싶은 일이 있다면 무엇인가요?", "학업/직장과 관련된 의무는 복제인간 친구에게 맡겨도 괜찮아요. 꼭 직접 행하고 싶은 무언가가 있나요?", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (9, "조금 오만해보여도 괜찮으니, 나에 대한 자랑거리를 적어보아요.", "아주 사소한 것이어도 괜찮아요. 오늘 이 공간에 나에 대한 자랑거리를 늘어놓아봅시다. ", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (10, "내가 원하는 부캐를 만들어볼 수 있다면, 어떤 모습의 나를 만나고 싶나요?", "부끄러워서, 자신없어서, 행하길 주저했던 행동이 있지는 않나요? 나날에서는 그 어떤 모습의 당신도 환영받을 수 있어요.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (11, "나는 어떤 모순을 지녔나요?", "배려심 깊은 이들도 가끔은 스스로의 이기적인 모습을 발견할 때가 있죠. 당신은 어떤 모순을 지닌 사람인가요? 그 누구도 당신을 탓하지 않을테니, 우리 함께 조용히 고백해보아요.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (12, "어떤 드라마 속의 삶을 꿈꿔보았나요?", "그 드라마 속 인물로 살아보고 싶어, 라는 생각을 해본 적이 있나요?", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (13, "요즘 새롭게 배워보고 싶은 것이 있다면 무엇인가요?", "당신의 최근 관심사가 궁금하답니다.", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (14, "이번주에 들은 칭찬이 있다면, 가장 기억에 남는 칭찬은 무엇인가요?", "떠오르는 칭찬이 없다면, 이번 기회에 나에게 칭찬 한 마디 건네주세요!", 1);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (15, "이번주 중 부족함이 있었다면, 그것은 무엇인가요?", "성취를 방해하는 요소가 있었는지 돌아보아요. 내 안의 고민일 수도, 나를 둘러싼 환경의 이유일 수도 있답니다.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (16, "당신의 다음 목표는 무엇인가요?", "현재의 목표를 완수하면, 그 후에는 어떤 것을 이루어내고 싶나요? '다음목표'를 인지하는 것은, '현 목표'에 대한 이행의 원동력이 되어주기도 한답니다.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (17, "지금 목표로 하는 일은, 내가 분주할 만한 가치가 있는 일인가요?", "당신이 목표로 하는 것은 얼마만큼의 중요성을 보이나요?", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (18, "목표를 성취하기 까지의 길에, 당신에게 가장 중요한 것은 무엇인가요?", "당신이 소중히 여기는 가치에 관한 것일 수도, 혹은 성취까지의 기간/효율에 관한 것일 수도 있어요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (19, "당신의 목표는 장기적/단기적 목표 중 어떤 분류에 속하나요?", "얼마나 먼 목표가 장기목표인지에 대한 기준은 중요치 않아요. 나의 삶을 미리 그려보고, 앞으로의 방향성을 찾아낼 수만 있다면 의미있는 목표가 되지 않을까요?", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (20, "긍정적인 자극을 주는 동료가 있나요?", "더 빠른 성취, 더 많은 성취를 원하게 만드는 동료가 있나요? 건설적인 자극을 주는 존재는 우리의 열정에 불을 지피죠.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (21, "떠올리기만 해도 괜시리 뿌듯해지는 성공경험이 있나요?", "작고 소중한 성공경험은 새로운 도전을 앞둔 당신의 '자신감 창고'가 되어줄 거예요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (22, "성취를 향한 길에 꼭 해결해야만 하는 문제가 있다면 무엇인가요?", "목표에 대한 명확한 인지에 관한 것일 수도, 내 안의 고민과 갈등일 수도, 성취행위의 방식에 관한 것일 수도 있어요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (23, "별똥별이 떨어지고 있습니다. 당신은 어떤 소원을 빌고 싶나요?", "단 몇 초만에 외칠 수 있는 소원이라면, 당신의 간절함이 그 순간으로 데려다 줄 거예요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (24, "목표란 당신에게 어떤 의미인가요?", "목표를 이루는 것이 당신의 삶에 어떤 의미를 갖고 있는지 생각해보아요. 조금 어렵다면, 목표완수와 '행복'의 관계에 대해 떠올려보는 것도 좋아요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (25, "목표를 향한 길에 동행하고 싶은 사람이 있나요?", "같은 목표를 향해 함께 노력하고 있는 친구가 있나요? 당신의 여정이 부디 외롭지 않기를 바라요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (26, "목표를 성취하기 위해, 지금 내가 할 수 있는 행동은 어떤 것이 있을까요?", "성취를 위한 행동을 잘게 쪼개어 보아요. 다음에 취해야할 이상적인 행동이 무엇일지 명확히 인지하는 것은 큰 힘이 있답니다.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (27, "열심히 달리고 있는 당신, 성취에 대한 권태기를 느껴던 적이 있나요?", "성취에 대한 권태기를 느꼈다는 건, 어쩌면 당신이 정말 열렬히 노력해왔음을 의미할지도 몰라요. 이런 순간에는 주위환경을 바꾸어 보거나, 다음 목표에 대한 구상을 하는 것이 도움이 되어요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (28, "목표한 바가 명확한 당신, 이번주에 가장 몰입했던 순간은 언제인가요?", "나의 움직임과 나의 나아감, 나의 계획과 나의 루틴에만 푹 빠져있던 순간이 있었나요?", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (29, "이번주가 다 지나기 전에 꼭 하고 싶은 일이 있다면 무엇인가요?", "꼭 성취를 위한 것이 아니어도 좋아요! 마저 행하고 싶은 일이 있다면 들려주세요.", 2);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (30, "나는 이번주 어떤 두려움을 느꼈나요?", "두려움은 스스로를 작아지게 만들죠. 하지만 어떤 것에 대한 두려움인지 명확히 알게되는 순간, 그 감정의 크기가 조금 작아졌다는 걸 발견하게 될 겁니다.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (31, "나는 무엇에서 벗어나면 제일 마음이 편할까요?", "감정정리라는 회고의 목적을 선택하게 된, 특별한 이유가 있다면 들려주세요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (32, "나를 진짜 평온하게 만드는 것들은 무엇일까요?", "스스로의 감정을 무던히 관리하는 방법이 있다면 들려주세요. 따뜻한 햇살에 몸을 녹이는 것일 수도, 좋아하는 노래를 들으며 청소기를 돌리는 것일 수도 있습니다.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (33, "한 통의 편지를 써봐요. 마음을 건네받을 사람은 당신이죠.", "감정적으로 지쳐있을 스스로에게 위로를 건네어 보아요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (34, "꿀꿀한 기분을 반전시키는 당신만의 비법이 있나요?", "새콤한 젤리 한 입, 평소와 다른 스타일링 등 나만 알고 있는 치트키가 있다면 소개해주세요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (35, "나는 스스로에게 상냥한 사람인가요?", "가끔은 스스로 되뇌는 말이 더 큰 상처가 될 때가 있어요. 당신은 스스로에게 어떤 말을 건네는 사람인가요?", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (36, "오늘 밤은 어떤 꿈을 꾸고 싶나요?", "부디 편안한 밤을 보내길 바라요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (37, "감정의 롤로코스터를 함께 견뎌주고 있는 사람이 있나요?", "우린 종종 스스로의 힘듦에만 주목하곤 하죠. 당신의 감정기복을 함께 버티고 있는 사람이 있는지 돌아봅시다. 나와 그를 위해 감정의 중심을 잡는 연습을 해보아요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (38, "당신이 만약 당신의 부모라면, 지금 어떤 말을 해주고 싶나요?", "부모님의 입을 빌려 스스로에게 하고픈 말을 적어보아요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (39, "내게 지금까지 남아있는 비난의 말은 무엇입니까?", "당신을 아프게 하는 기억은 이곳에 두고 가세요. 사랑의 말만 듣기에도 모자란 삶이랍니다.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (40, "감정기복으로 스트레스를 받았던 경험이 있나요? 그때 나는 어떻게 대처했나요?", "스트레스를 극복했던 경험을 떠올려봅시다. 어쩌면 지금의 상황에 적용해볼 수 있는 '극복의 힌트'를 얻게될 지도 몰라요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (41, "지난 일주일의 대표 BGM을 선정해볼까요?", "나의 감정을 드러낼 수 있는 노래가 있다면 소개해주세요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (42, "스스로 제어하고 싶은 감정이 있나요? 그 이유가 무엇인가요?", "감정에 휘둘리기 보다, 감정의 주인이 되길 바라요.", 3);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (43, "누군가와의 관계가 힘들 때, 당신의 선택과 그 기준은 무엇인가요?", "스스로를 지키기 위한 '관계의 규칙'을 정해보는 것이 도움이 될 때가 있죠.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (44, "다시 기회가 주어진다면 용서하거나, 용서받고 싶은 사람이 있나요?", "당시엔 어렸고, 미숙했다 라는 말로 끝맺음하기엔.. 유독 그리운 관계가 있죠.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (45, "누군가와 가까워지기 위해 노력해본 경험이 있나요?", "그런 노력을 이끌어내는 이들은 어떤 공통점을 가지고 있나요? 당신은 어떤 유형의 사람을 좋아하나요? 그리고 어떤 방법으로 다가갔나요?", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (46, "삶에 치여 힘들어하고 있을 때, 누가 당신의 표정을 주시하고 있나요?", "당신의 곁을 맴돌며 당신이 행복하길 바라는 누군가가 있나요? 삶에 지칠 땐, 그 사람의 곁에서 잠시 쉬어가는 것도 좋은 방법이에요.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (47, "관계라는 것은 당신에게 어떤 의미인가요?", "자아실현, 경제적 안정, 사회에의 공헌 등 인생의 다른 가치와 비교해 생각해봅시다.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (48, "불안하게 할 리 없는 든든한 친구를 만났나요?", "자주 만나는 사이가 아니라도 괜찮아요. 당신과의 단단하고 소중한 추억을 공유한, 그런 친구가 있나요?", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (49, "무조건적인 믿음을 받고싶은 상대가 있나요?", "더 가까워지고픈, 더 단단해지고픈 관계가 있나요?", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (50, "관계 속에서 질투를 느껴본 적이 있나요?", "'괜히 질투가 날 만큼 당신과의 관계가 소중해요'라는 한 마디가 관계문제의 해결책이 되어줄 수도 있어요.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (51, "당신은 요즘 어떤 이의 이야기를 듣고 싶나요?", "누군가의 생각과 감정이 궁금해진 일이 있었나요? 솔직한 대화의 시간을 가져보는 것을 추천드려요.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (52, "소외감 때문에 움츠러 들었던 적이 있나요?", "소속감이 중요해지는 때가 있죠. 혹시나 외로운 시간을 견디진 않았을 지 걱정했어요.", 4);
INSERT INTO extra_question(question_id, content, help, goal_id) VALUES (53, "당신 곁에 '추가로' 있었으면 하는 존재가 있나요?", "이번엔 새로 만나고 싶거나 갖고싶은 관계에 대해 생각해봅시다. 가령 외동에게의 언니/형 같은 존재랄까요.", 4);