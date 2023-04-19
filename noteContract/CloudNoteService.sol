pragma solidity ^0.4.0;


contract CloudNoteService{

    mapping(string => mapping(string => string)) private data;

    function CloudNoteService()  {
    }

    // Add a new note
    function add(string title, string author, string content) public {
        require(keccak256(title) != keccak256(""), "title cannot be empty");
        require(keccak256(author) != keccak256(""), "author cannot be empty");
        require(keccak256(content) != keccak256(""), "content cannot be empty");
        require(keccak256(data[title][author]) == keccak256(""), "note can not exist");

        data[title][author] = content;
    }

    function update(string title, string author,string content) public{
        require(keccak256(title) != keccak256(""), "title cannot be empty");
        require(keccak256(author) != keccak256(""), "author cannot be empty");
        require(keccak256(data[title][author]) != keccak256(""), "note must exist");

        data[title][author] = content;
    }

    function remove(string title, string author) public{
        require(keccak256(title) != keccak256(""), "title cannot be empty");
        require(keccak256(author) != keccak256(""), "author cannot be empty");
        require(keccak256(data[title][author]) != keccak256(""), "note must exist");

        delete data[title][author];
    }

    function get(string title, string author) public view returns (string){
        require(keccak256(title) != keccak256(""), "title cannot be empty");
        require(keccak256(author) != keccak256(""), "author cannot be empty");
        require(keccak256(data[title][author]) != keccak256(""), "note must exist");

        return data[title][author];
    }

}