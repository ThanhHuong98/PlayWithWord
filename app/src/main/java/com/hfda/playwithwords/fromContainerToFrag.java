package com.hfda.playwithwords;
//mục đích chính của cái interface này là Activity sẽ đọc dữ liệu từ database rồi gửi xuống Fragment bên dưới
public interface fromContainerToFrag
{
    void InfoToHandle(String mess, String roundOfMode,String newAnswer,String newQuestion,String newTranscript,String newDefinition,String[] newAnswerBtn);
}
