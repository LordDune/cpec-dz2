#### Задание 1. Полностью разобраться с кодом программы разработанной на семинаре, переписать программу.


#### Задание 2. Переработать метод проверки победы


Добавлена переменная **private static int winCount** // количество фишек для победы

Переделан метод **checkWin(char c)** таким образом, чтобы проверка на победу осуществлялась для любого размера поля и для любого количества фишек, необходимых для победы. Метод проходит по всему игровому полю и считает количество идущих подряд выигрышных фишек на каждой горизонтали, вертикали и диагонали. Если количество подряд идущих фишек достигает количество, необходимое для победы, определяется победитель.

#### Задание 3. Доработать искусственный интеллект, чтобы он мог блокировать ходы игрока.

Доработан метод aiTurn(), который разбит на 5 этапов:
1. Проходя по каждой пустой ячейке, ставит туда свою фишку и проверяет, нет ли выигрышного хода для него, и если да, то делает этот ход
2. Проходя по каждой пустой ячейке, ставит туда фишку человека и проверяет, нет ли выигрышного хода для противника, и если да, то мешает этому ходу
3. Пункт 2, только с глубиной в 2 хода. Метод обернут циклом, в рамках которого компьютер ставит еще одну воображаемую фишку на поле. И если такой ход приводит к победе, то он делает ход
4. Пункт 3, только с глубиной в 2 хода
5. Если никакой из условий 2-5 не выполнилось, ставит фишку в случайное пустое поле.