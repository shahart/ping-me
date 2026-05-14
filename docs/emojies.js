
    function escapeHtml(text) {
        return text
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;")
            .replaceAll("'", "&#39;");
    }

    function setResultStatus(message) {
        document.getElementById("result-status").textContent = message;
    }

    function renderResultCards(items, textValue) {
        if (!items.length) {
            setResultStatus("No matches found");
            document.getElementById("result").innerHTML =
                "<div class=\"empty-state\">No emoji entries were matched. Try a single emoji or a shorter sequence.</div>" +
                "<div class=\"result-links\">" +
                "<a class=\"pill-link\" href=\"https://emojigraph.org/he/search/?q=" + encodeURIComponent(textValue) + "&searchLang=he\" target=\"_blank\">Search in Emoji Graph</a>" +
                "</div>";
            return;
        }

        setResultStatus(items.length + " match" + (items.length === 1 ? "" : "es") + " found");

        let cards = items.map(function(item) {
            let linkHtml = item.aliasSlug
                ? "<a class=\"emoji-link\" href=\"https://emojigraph.org/he/" + item.aliasSlug + "/\" target=\"_blank\">Open " + escapeHtml(item.emoji) + " in Emoji Graph</a>"
                : "";

            return "<article class=\"emoji-card\">" +
                "<div class=\"emoji-row\">" +
                "<div class=\"emoji-glyph\">" + escapeHtml(item.emoji) + "</div>" +
                "<div class=\"emoji-meta\">" +
                "<p class=\"emoji-label\">" + escapeHtml(item.description) + "</p>" +
                linkHtml +
                "</div>" +
                "</div>" +
                "</article>";
        }).join("");

        let links = "<div class=\"result-links\">" +
            "<a class=\"pill-link\" href=\"https://emojigraph.org/he/search/?q=" + encodeURIComponent(textValue) + "&searchLang=he\" target=\"_blank\">Search in Emoji Graph</a>" +
            "</div>";

        document.getElementById("result").innerHTML = "<div class=\"result-list\">" + cards + "</div>" + links;
    }

    function click() {
        let textValue = document.getElementById('emoji').value;
        if (textValue == 'Speed') {
            // speed feature on puzzles-edu doesn't work from the textArea there, temp code to know the iWatch 9 series speed
            let dt = new Date();
            var amount = 150000000 / 1;
            let start = dt.getTime();
            for (var i = amount; i > 0; i--) {
                // do nothing
            }
            dt = new Date();
            let res = 'For-loop till ' + amount +
                '\nTime taken [sec] ' + (dt.getTime() - start) / 1000;
            setResultStatus("Benchmark result");
            document.getElementById("result").innerHTML = "<div class=\"speed-state\">" + escapeHtml(res) + "</div>";
            return;
        }
        textValue = textValue.trim();
        let items = [];
        for (var i = 0; i < textValue.length; i++) {
            try {
                let emoj = textValue.charAt(i) + textValue.charAt(i+1);
                let dictValue = dict[emoj];
                if (dictValue) {
                    let firstAlias = "";
                    let lastIdx = dictValue.lastIndexOf(":", dictValue.length - 3);
                    if (lastIdx >= 0) {
                        firstAlias = dictValue.substring(lastIdx+1, dictValue.length - 2).trim().replaceAll("_", "-");
                    }
                    i++;

                    items.push({
                        emoji: emoj,
                        description: (heDict[emoj] ? heDict[emoj] + ": " : "") + dictValue.trim(),
                        aliasSlug: firstAlias
                    });
                }
            }
            catch (err) {
                alert("at index " + i + " --> " + err);
            }
        }
        renderResultCards(items, textValue);
    }

    document.getElementById("alias").addEventListener('click', function(e) {
        click();
    });

    document.getElementById("emoji").addEventListener("keydown", function(e) {
        if (e.key === "Enter") {
            click();
        }
    });

    click();

