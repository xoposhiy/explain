# Цель проекта #
Создать платформу, на базе которой можно было бы создавать трансляторы формальных языков с гибкими возможностями их расширения.

# Аналоги #
Последнее время появляются языки программирования, такие как Nemerle и Boo, включающие механизм макросов, с помощью которого можно расширять синтаксис этих языков. Кроме того,  трансляторы этих языков, предоставляют специально выделенные точки расширения, через которые можно немного изменять его поведение.

Все эти методы кажутся довольно ограниченными по своим возможностям и не предоставляют общего решения проблемы гибкого расширения синтаксиса. Однако, надо признать, эти возможности покрывают довольно большую часть потребностей в расширении синтаксиса.

# Уникальность #
Explain - это попытка поработать альтернативный подход к созданию расширяемых языков. В этом подходе расширяется непосредственно набор известных лексическому анализатору лексем и грамматика, которой непосредственно руководствуется синтаксический анализатор. Это предоставляет полную свободу расширения.

Однако свобода расширения строго ограничивается, если она может приводить к опасным возможностям. В частности большое внимание уделяется проблеме прямой и обратной совместимости расширенного транслятора и исходного трансляторов. В идеале:
  * Расширенная версия транслятора должна обрабатывать программы на исходном языке в точности так же как и исходный транслятор, если это не противоречит самой сути рашсирения.
  * Исходный транслятор должен корректно обрабатывать программы на расширенной версии языка, если это в принципе возможно.

Плаформа Explain намерена предоставлять удобные инструменты, чтобы можно было создавать расширения, удовлетворяющие прямой и обратной совместимости.

# Методы #
Explain - экспериментальный проект. Одна из его главных задача - исследовать возможность
создания и целесообразность использования подобных платформ. Поэтому при разработке функциональность может жертвоваться в пользу скорости разработки. В частности поэтому в качестве основы синтаксического анализатора был взят LL(1)-анализатор, распознающий заведомо меньший класс языков, чем LR-анализаторы. LL(1) - оказался гораздо более удобен для расширения и прост в понимании и реализации - это являлось главными определяющими факторами.