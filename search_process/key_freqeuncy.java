package search_process;

public class key_freqeuncy implements Comparable<key_freqeuncy> {
    /////검색어와 그 빈도수를 필드로 가지는 클래스, 비교 가능하게끔 Comparable을 implement함
    private String search_key;
    private int freq;

    public key_freqeuncy(String search_key) {
        this.search_key = search_key;
        this.freq = 1;
    }

    public key_freqeuncy(String search_key, int freq) {
        this.search_key = search_key;
        this.freq = freq;
    }

    public String getSearch_key() {
        return search_key;
    }

    public int getFreq() {
        return freq;
    }

    public void one_more() {
        this.freq += 1;
    }
    ///빈도 수 1 증가

    @Override
    public int compareTo(key_freqeuncy other) {
        // Compare the current object's value with the other object's value
        // and return a negative integer, zero, or a positive integer accordingly.
        System.out.println(this.freq);
        System.out.println(other.freq);
        return Integer.compare(this.freq, other.freq);

    }
}
