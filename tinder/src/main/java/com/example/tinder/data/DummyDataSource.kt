package com.example.tinder.data

import com.example.tinder.R

object DummyDataSource {

    val dummyUsersFull = mutableListOf(
        //regualer guy
        DummyUserFull(2, "Adam Doe", 25, "New York", R.drawable.adam, "I love coding", "Always overthinks simple decisions, once spent an hour choosing a coffee cup, and writes long comments in group chats.", 1998, "2025-08-17", 2, 5, 1),
        //robot
        DummyUserFull(3, "Adam Smith", 30, "Los Angeles", R.drawable.adam01, "Music fan", "Claims to be half human, half robot, often hums like a broken synthesizer, and organizes playlists by mood, weather, and temperature.", 1993, "2025-08-17", 0, 3, 0),
        //cyborg
        DummyUserFull(4, "Adam Johnson", 47, "Chicago", R.drawable.adam02, "Traveler", "Collects tiny souvenirs from every city he visits, argues with hotel staff over pillow fluffiness, and always forgets his own passport.", 1976, "2025-08-17", 1, 2, 1),
        //anime girl
        DummyUserFull(5, "Adam Brown", 29, "Houston", R.drawable.adam03, "Foodie", "Can name every ingredient in a pizza, eats cereal for dinner while reading cookbooks, and once tried to make sushi with gummy bears.", 1994, "2025-08-17", 0, 1, 0),
        //gorilla
        DummyUserFull(6, "Adam Davis", 33, "Phoenix", R.drawable.adam04, "Gamer", "Plays video games like a professional, forgets to eat, and gives elaborate backstories to in-game characters that no one asked for.", 1990, "2025-08-17", 1, 4, 0),
        //Yeti thing
        DummyUserFull(7, "Adam Wilson", 40, "Philadelphia", R.drawable.adam05, "Runner", "Runs marathons even when it rains, collects medals he never shows anyone, and sometimes talks to his running shoes for motivation.", 1983, "2025-08-17", 0, 2, 0),
        //women with mustache
        DummyUserFull(8, "Adam Taylor", 28, "San Antonio", R.drawable.adam06, "Artist", "Paints abstract art that confuses his friends, keeps brushes in odd places, and once painted a wall by accident while daydreaming.", 1995, "2025-08-17", 2, 3, 1),
        //serious girl with koitlin badge
        DummyUserFull(9, "Adam Anderson", 35, "San Diego", R.drawable.adam07, "Nature lover", "Spends weekends hugging trees, writes poems about squirrels, and has an intense debate with himself over which is the best bird.", 1988, "2025-08-17", 0, 1, 0),
        // mouse in a suit
        DummyUserFull(10, "Adam Thomas", 32, "Dallas", R.drawable.adam08, "Bookworm", "Reads books faster than anyone can blink, labels every shelf meticulously, and whispers plot twists to random strangers at coffee shops.", 1991, "2025-08-17", 1, 5, 0),
        //a man but he just peed himself
        DummyUserFull(11, "Adam Martinez", 27, "San Jose", R.drawable.adam09, "Tech geek", "Builds gadgets that do nothing useful, invents new programming languages for fun, and once tried to automate making toast.", 1996, "2025-08-17", 0, 3, 0),
        //radical guy
        DummyUserFull(12, "Adam Garcia", 31, "Austin", R.drawable.adam10, "Hiker", "Gets lost on trails often, insists every walk is an adventure, and brings more snacks than actual water.", 1992, "2025-08-17", 0, 4, 1),
        //low resolution guy
        DummyUserFull(13, "Adam Rodriguez", 26, "Jacksonville", R.drawable.adam11, "Photographer", "Takes photos of things no one notices, edits them obsessively, and always carries three cameras and two tripods for a ten-minute walk.", 1997, "2025-08-17", 1, 2, 0),
        //anime guy no beard
        DummyUserFull(14, "Adam Lee", 36, "Fort Worth", R.drawable.adam12, "Dog lover", "Talks to dogs as if they understand business strategies, gives them nicknames like CEO and Manager, and tries to teach them calculus.", 1987, "2025-08-17", 0, 3, 0),
        //same guy as above but with beard
        DummyUserFull(15, "Adam Walker", 42, "Columbus", R.drawable.adam13, "Cyclist", "Rides a bike to everywhere including grocery stores, wears a helmet at all times, and challenges squirrels to races.", 1981, "2025-08-17", 2, 4, 1),
        //ninja from naruto hidden leaf
        DummyUserFull(16, "Adam Hall", 29, "Charlotte", R.drawable.adam14, "Baker", "Bakes bread shaped like dinosaurs, forgets the yeast sometimes, and once made a cake that accidentally exploded.", 1994, "2025-08-17", 0, 1, 0),
        //suave man
        DummyUserFull(17, "Adam Allen", 34, "San Francisco", R.drawable.adam15, "Writer", "Writes short stories about chairs, attends book clubs alone just to argue with himself, and keeps a notebook for every sidewalk crack he finds interesting.", 1989, "2025-08-17", 1, 2, 0),
        //man from kill bill track suit
        DummyUserFull(18, "Adam Young", 38, "Indianapolis", R.drawable.adam16, "Collector", "Collects socks from different countries, organizes them by smell, and once traded a sandwich for a rare sock.", 1985, "2025-08-17", 0, 3, 0),
        //man from yu gi oh
        DummyUserFull(19, "Adam Hernandez", 27, "Seattle", R.drawable.adam17, "Swimmer", "Swims in bathtubs for practice, counts soap bubbles as laps, and sometimes wears goggles to work.", 1996, "2025-08-17", 1, 4, 0),
        //pokemon trainer
        DummyUserFull(20, "Adam King", 41, "Denver", R.drawable.adam18, "Climber", "Climbs office chairs to feel taller, practices rope climbing in living rooms, and once got stuck on a ladder for three hours.", 1982, "2025-08-17", 0, 2, 1),
        //lobster man
        DummyUserFull(21, "Adam Wright", 33, "Washington", R.drawable.adam19, "Teacher", "Gives pop quizzes to pets, writes elaborate lesson plans for houseplants, and once tried to teach a rock multiplication.", 1990, "2025-08-17", 2, 3, 0),
        //wizard
        DummyUserFull(22, "Adam Lopez", 39, "Boston", R.drawable.adam20, "Chef", "Experiments with spaghetti ice cream, labels spices with mysterious codes, and insists on tasting everything three times.", 1984, "2025-08-17", 1, 2, 1),
        //firefighter
        DummyUserFull(23, "Adam Hill", 28, "El Paso", R.drawable.adam21, "Singer", "Sings to traffic lights to improve rhythm, carries a portable karaoke machine, and judges birds’ singing contests.", 1995, "2025-08-17", 0, 3, 0),
        //pilot
        DummyUserFull(24, "Adam Scott", 37, "Nashville", R.drawable.adam22, "Dancer", "Dances in grocery store aisles, choreographs for his dog, and practices pirouettes on escalators.", 1986, "2025-08-17", 1, 4, 0),
        //rat catcher
        DummyUserFull(25, "Adam Perez", 34, "Detroit", R.drawable.adam23, "Coffee lover", "Can taste coffee blindfolded, memorizes coffee shop menus, and once tried to roast beans on his stovetop.", 1991, "2025-08-17", 0, 2, 1)
    )
}




