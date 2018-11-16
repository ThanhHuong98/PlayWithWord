package com.hfda.playwithwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.os.Build.ID;
import static android.provider.Contacts.SettingsColumns.KEY;
import static java.sql.Types.INTEGER;
import static java.text.Collator.PRIMARY;

public class MyDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME="Database";
    private static final int DB_VERSION=1;
    MyDatabase(Context context)
    {
        //Khởi tạo database
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Tạo bảng chứa dữ liệu bộ câu hỏi
        db.execSQL("CREATE TABLE IF NOT EXISTS DATA ("
                +"ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"ENGLISH_TEXT TEXT, "
                +"TRANSCRIPTION TEXT, "
                +"VN_TEXT TEXT, "
                +"DESCRIPTION TEXT, "
                +"IMAGE INTEGER, "
                +"AUDIO INTEGER);");

        //Tạo bảng chứa thông tin user
        db.execSQL("CREATE TABLE IF NOT EXISTS USER_INFO ("
                +"ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"USERNAME TEXT, "
                +"PASSWORD TEXT, "
                +"TOTAL_SCORE INTEGER);");
        //Tạo bảng mode 5

        db.execSQL("CREATE TABLE IF NOT EXISTS MODE5 ("+"ID INTEGER PRIMARY KEY AUTOINCREMENT,"
        +"VIETNAMESE TEXT,"+"ENGLISH TEXT);");
        //Thêm dữ liệu vào bộ câu hỏi

        InsertData(db,"parrot","/ˈpærət/","con vẹt",
                "a tropical bird with a curved beak",R.mipmap.image1,R.raw.sound1);
        InsertData(db,"tortoise","/ˈtɔːrtəs/","con rùa",
                "a reptile with a hard round shell, that lives on land and moves very slowly.",R.mipmap.image2,R.raw.sound2);
        InsertData(db,"lion","/ˈlaɪən/","sư tử",
                "a large powerful animal of the cat family, the male ones have a mane",R.mipmap.image3,R.raw.sound3);
        InsertData(db,"elephant","/ˈelɪfənt/","con voi",
                "a large animal with thick grey skin, large ears, tusks and a trunk",R.mipmap.image4,R.raw.sound4);
        InsertData(db,"cheetah","/ˈtʃiːtə/","con báo",
                "a wild animal of the cat family, with black spots, that runs very fast",R.mipmap.image5,R.raw.sound5);
        InsertData(db,"antelope","/ˈæntɪloʊp/","linh dương",
                "an African or Asian animal like a deer, that runs very fast.",R.mipmap.image6,R.raw.sound6);
        InsertData(db,"cicada","/sɪˈkeɪdə/","con ve",
                "a large insect in hot countries, the male ones make continuous sound in summer",R.mipmap.image7,R.raw.sound7);
        InsertData(db,"bee","/biː/","con ong",
                "a black and yellow flying insect that can sting.",R.mipmap.image8,R.raw.sound8);
        InsertData(db,"goldfish","/ˈɡoʊldfɪʃ/","cá vàng",
                "a small orange or red fish that is kept as pets in bowls or ponds.",R.mipmap.image9,R.raw.sound9);
        InsertData(db,"ant","/ænt/","con kiến",
                "a small insect that lives in highly organized groups.",R.mipmap.image10,R.raw.sound10);
        InsertData(db,"mosquito","/məˈskiːtoʊ/","con muỗi",
                "a flying insect that bites humans and animals and sucks their blood.",R.mipmap.image11,R.raw.sound11);
        InsertData(db,"spider","/ˈspaɪdər/","con nhện",
                "a small creature with eight thin legs, spins webs to catch insects for food.",R.mipmap.image12,R.raw.sound12);
        InsertData(db,"alligator","/ˈælɪɡeɪtər/","cá sấu",
                "a large reptile with hard skin and very big jaws, that lives in rivers and lakes ",R.mipmap.image13,R.raw.sound13);
        InsertData(db,"dolphin","/ˈdɑːlfɪn/","cá heo",
                "a sea animal that looks like a large fish with a pointed mouth",R.mipmap.image14,R.raw.sound14);
        InsertData(db,"duck","/dʌk/","con vịt",
                "a bird that lives on or near water and has webbed feet and a wide beak.",R.mipmap.image15,R.raw.sound15);
        InsertData(db,"sheep","/ʃiːp/","con cừu",
                "an animal with a thick coat, kept on farms for its meat or its wool",R.mipmap.image16,R.raw.sound16);
        InsertData(db,"camel","/ˈkæml/","lạc đà",
                "an animal with a long neck and one or two humps on its back",R.mipmap.image17,R.raw.sound17);
        InsertData(db,"snail","/sneɪl/","ốc sên",
                "a small soft creature with a hard round shell on its back, that moves very slowly",R.mipmap.image18,R.raw.sound18);
        InsertData(db,"monkey","/ˈmʌŋki/","con khỉ",
                "an animal with a long tail, that climbs trees and lives in hot countries.",R.mipmap.image19,R.raw.sound19);
        InsertData(db,"snake","/sneɪk/","con rắn",
                "a reptile with a very long thin body and no legs.",R.mipmap.image20,R.raw.sound20);
        InsertData(db,"octopus","/ˈɑːktəpʊs/","bạch tuột",
                "a sea creature with a soft round body and eight long arms",R.mipmap.image21,R.raw.sound21);
        InsertData(db,"frog","/frɑːɡ/","con ếch",
                "an amphibian with smooth skin, have very long back legs for jumping.",R.mipmap.image22,R.raw.sound22);
        InsertData(db,"owl","/aʊl/","con cú",
                "a bird of prey with large round eyes, that hunts at night.",R.mipmap.image23,R.raw.sound23);
        InsertData(db,"squirrel","/ˈskwɜːrə/","con sóc",
                "a small animal with a long thick tail, eats nuts and lives in trees.",R.mipmap.image24,R.raw.sound24);
        InsertData(db,"giraffe","/dʒəˈræf/","hươu cao cổ",
                "a tall African animal with a very long neck, long legs",R.mipmap.image25,R.raw.sound25);
        InsertData(db,"panda","/ˈpændə/","gấu trúc",
                "a large black and white animal like a bear, that lives in China and is very rare",R.mipmap.image26,R.raw.sound26);
        InsertData(db,"cow","/kaʊ/","con bò",
                "a large animal kept on farms to produce milk or beef",R.mipmap.image27,R.raw.sound27);
        InsertData(db,"wolf","/wʊlf/","con sói",
                "large wild animal of the dog family, that lives and hunts in groups",R.mipmap.image28,R.raw.sound28);
        InsertData(db,"rabbit","/ˈræbɪt/","con thỏ",
                "a small animal with soft fur, long ears and a short tail.",R.mipmap.image29,R.raw.sound29);
        InsertData(db,"zebra","/ˈziːbrə/","ngựa vằn",
                "an African wild animal like a horse with stripes on its body",R.mipmap.image30,R.raw.sound30);




        //thêm dữ liệu cho mode 5
        InsertMode5(db,"Cô ấy đang đợi chuyến tàu","She is waiting for the train");
        InsertMode5(db,"Đây là một cuốn sách màu đỏ","This is a red book");
        InsertMode5(db,"Hôm nay trời rất lạnh","It's very cold today");
        InsertMode5(db,"Tôi đã sẵn sàng cho bữa sáng","I'm ready for breakfast");
        InsertMode5(db,"Anh ta rất giỏi chơi tennis","He's good at tennis");
        InsertMode5(db,"Chiếc mũ của bạn trông rất đẹp","Your hat looks very nice");
        InsertMode5(db,"Tôi bị đau răng","I have toothache");
        InsertMode5(db,"Chúng ta có một chiếc xe ô tô đợi bên ngoài","We have a car waiting outside");
        InsertMode5(db,"Có 2 chiếc bút chì ở trong hộp của tôi","There are two pencils in my box");
        InsertMode5(db,"Chiếc lọ được làm bằng thủy tinh","The vase is made of glass");
        InsertMode5(db,"Tôi làm cho ngân hàng","I work for bank");
        InsertMode5(db,"Trời có vẻ sắp mưa","It's likely to rain");
        InsertMode5(db,"Cô ấy hi vọng sẽ có được một công việc","She hopes to get a job");
        InsertMode5(db,"Tôi bắt đầu làm việc vào 7 giờ","I start working at 7");
        InsertMode5(db,"Cô ấy xinh đẹp làm sao","How beautiful she is");
        InsertMode5(db,"Cậu ta trẻ tuổi hơn tôi","He is younger than I");
        InsertMode5(db,"Tốn khoảng 10 phút để đến được đó","It takes ten minutes to get there");
        InsertMode5(db,"Tôi sẽ ở đây cho đến khi bạn quay lại","I'll stay here until you come back");
        InsertMode5(db,"Anh ấy rất thông minh","He is very intelligent");
        InsertMode5(db,"Đây là một tình huống khó khăn","This is a difficult situation");
        InsertMode5(db,"Hãy đặt mấy cuốn sách lên bàn","Put the books on the table");
        InsertMode5(db,"Cậu đã phá luật","You broke the law");
        InsertMode5(db,"Hãy nói cho tôi sự thật ngay lập tức","Tell me the truth right now");
        InsertMode5(db,"Tôi được sinh ra trong một ngôi làng nhỏ","I was born in the small village");
        InsertMode5(db,"Ăn cá tốt cho sức khỏe của bạn","Eating fish is good for your health");
        InsertMode5(db,"Tôi không thể nhớ cách sử dụng chiếc máy này","I can't remember how to use this machine");
        InsertMode5(db,"Tòa nhà này gần hoàn thành rồi","This building is near completion");
        InsertMode5(db,"Một cơn bão lớn đang ập đến","A big typhoon is approaching");
        InsertMode5(db,"Tôi rất hiếm khi sử dụng đồ ăn từ sữa","I seldom eat dairy products");
        InsertMode5(db, "Bạn Toàn là người hướng nội", "Toan is a introvert person");
        InsertMode5(db, "Hương vừa mới quở trách nhóm của cô ấy", "Huong just scolded at her team");
        InsertMode5(db, "Cô ấy thật nham hiểm", "She is so mean");
    }


    private void InsertData(SQLiteDatabase db, String engText, String transcription, String vnText, String discription, int imageId, int audioId)
    {
        ContentValues dataValues = new ContentValues();
        dataValues.put("ENGLISH_TEXT", engText);
        dataValues.put("TRANSCRIPTION", transcription);
        dataValues.put("VN_TEXT", vnText);
        dataValues.put("DESCRIPTION", discription);
        dataValues.put("IMAGE", imageId);
        dataValues.put("AUDIO", audioId);
        db.insert("DATA", null, dataValues);
    }
    private void InsertMode5(SQLiteDatabase db, String vietnamese, String english)
    {
        ContentValues dataValues=new ContentValues();
        dataValues.put("VIETNAMESE",vietnamese);
        dataValues.put("ENGLISH",english);
        db.insert("MODE5",null,dataValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //ứng dụng của mình chưa cần update nên hàm này để trống
    }
}
