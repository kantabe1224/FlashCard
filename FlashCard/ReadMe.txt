1. 問題を書く
 carddata.txt
 "---" で区切って、問題、解答の順でテキストファイルに書く。
 梱包物を参考に。
 文字コードは utf8 でないと恐らく文字化けする。

2. アプリインストール
 FlashCard.apk

3. 問題をAndroid端末に格納
 carddata.txt
 Glaxy S SC-02B だと
   /mnt/sdcard/carddata.txt
 に格納する。(Environment.getExternalStorageDirectory()で取得されるパス以下)
 ファイル名は、ソース中で固定文字列なので変更不可。

