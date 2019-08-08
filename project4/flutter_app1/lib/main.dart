import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: "Satu",
      theme: ThemeData(
        primarySwatch: Colors.lightBlue
      ),
      home: Scaffold(
        appBar: AppBar(
          title: Text("Probus System"),
        ),
        body: Container(

        ),
      ),
    );
  }
}